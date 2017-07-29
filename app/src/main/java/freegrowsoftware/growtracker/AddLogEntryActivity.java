package freegrowsoftware.growtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.afollestad.materialcamera.MaterialCamera;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import freegrowsoftware.growtracker.objects.LogEntry;
import freegrowsoftware.growtracker.objects.Plant;
import freegrowsoftware.growtracker.Util.ImageUtil;
import io.blackbox_vision.materialcalendarview.view.CalendarView;

public class AddLogEntryActivity extends BaseActivity {
    private final static int CAMERA_RQ = 6969;
    private MaterialCamera camera;
    private String imagePath;
    private String editImage;
    private String editNote;
    private String editDesc;
    private Date setDate;
    private int editId;
    private Plant currentPlant;
    private Drawable drawable;
    private CalendarView dateCalendar;

    private EditText entryDescription;
    private EditText entryNotes;
    private ImageButton entryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data.containsKey("plant")) {
            currentPlant = (Plant) data.getSerializable("plant");
        }
        editImage = null;
        if (data.containsKey("log_image")) {
            editImage = data.getString("log_image");
            imagePath = editImage;
        }
        editNote = null;
        if (data.containsKey("log_notes")) {
            editNote = data.getString("log_notes");
        }
        editDesc = null;
        if (data.containsKey("log_desc")) {
            editDesc = data.getString("log_desc");
        }
        editId = 0;
        if (data.containsKey("log_id")) {
            editId = data.getInt("log_id");
        }

        entryDescription = (EditText) findViewById(R.id.entry_description);
        entryNotes = (EditText) findViewById(R.id.entry_notes);
        entryImage = (ImageButton) findViewById(R.id.add_image_button);
        dateCalendar = (CalendarView) findViewById(R.id.date_calendar);
        setDate = new Date();
        dateCalendar.setOnDateClickListener(new CalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(Date date) {
                setDate = date;
            }
        });
        entryNotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        if (editDesc != null) {
            entryDescription.setText(editDesc);
        }
        if (editNote != null) {
            entryNotes.setText(editNote);
        }
        if (editImage != null) {
            drawable = Drawable.createFromPath(editImage);
            entryImage.setImageDrawable(drawable);
        }

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("plant", currentPlant);
        setToolbarClickBackButton(PlantInfoActivity.class, map);

        setTitle("Add Log Entry");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_log_entry;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                imagePath = data.getDataString().replace("file:", "");
                ImageUtil.saveThumbnail(imagePath);


                drawable = Drawable.createFromPath(imagePath);
                entryImage.setImageDrawable(drawable);
            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (camera != null) {
                        camera = null;
                    }
                    camera = new MaterialCamera(this).stillShot();
                    camera.saveDir(getAlbumStorageDir());
                    camera.qualityProfile(MaterialCamera.QUALITY_LOW);
                    camera.start(CAMERA_RQ);
                    return;
                }
                return;
            }
        }
    }

    public void addImage(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        camera = new MaterialCamera(this).stillShot();
        camera.saveDir(getAlbumStorageDir());
        camera.start(CAMERA_RQ);
    }

    public File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "plant_images");
        if (!file.mkdirs()) {
            Log.i("INFO", "Directory not created");
        }
        return file;
    }

    private HashMap<String, String> getEntryValues() {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("DESCRIPTION", entryDescription.getText().toString());
        map.put("NOTE", entryNotes.getText().toString());

        return map;
    }

    private LogEntry createLogEntry(HashMap values) {
        if (editId == 0) {
            return new LogEntry(getApplicationContext(), new SimpleDateFormat("MM/dd/yyyy").format(setDate), values.get("DESCRIPTION").toString(), values.get("NOTE").toString(), imagePath);
        }
        return new LogEntry(getApplicationContext(), editId, new SimpleDateFormat("MM/dd/yyyy").format(setDate), entryDescription.getText().toString(), entryNotes.getText().toString(), imagePath);
    }

    public void saveEntry(View view) {
        HashMap data = getEntryValues();
        LogEntry entry = createLogEntry(data);
        entry.save(Integer.toString(currentPlant.getId()));
        goToPlantInfo();
    }

    private void goToPlantInfo() {
        Intent intent = new Intent(getApplicationContext(), PlantInfoActivity.class);
        intent.putExtra("plant", currentPlant);
        startActivity(intent);
        finish();
    }
}
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.afollestad.materialcamera.MaterialCamera;
import io.blackbox_vision.materialcalendarview.view.CalendarView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import freegrowsoftware.growtracker.Util.ImageUtil;
import freegrowsoftware.growtracker.database.DBHelper;

public class AddPlantActivity extends BaseActivity {
    private final static int CAMERA_RQ = 6969;

    private MaterialCamera camera;
    private String imagePath;
    private Date setDate;

    private ImageButton addImageButton;
    private LinearLayout addImageWrapper;
    private EditText plantName;
    private CalendarView dateCalendar;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(this);

        addImageButton = (ImageButton) findViewById(R.id.add_image_button);
        addImageWrapper = (LinearLayout) findViewById(R.id.add_image_wrapper);
        addImageWrapper.setVisibility(View.VISIBLE);
        plantName = (EditText) findViewById(R.id.plantName);
        dateCalendar = (CalendarView) findViewById(R.id.calendar_view);
        setDate = new Date();
        dateCalendar.setOnDateClickListener(new io.blackbox_vision.materialcalendarview.view.CalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(Date date) {
                setDate = date;
            }
        });

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarClickBackButton(PlantListActivity.class, null);

        setTitle("Add Plant");
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void addImage(View view) {
        // TODO(Sean): This duplicates
        if (camera != null) {
            camera = null;
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        camera = new MaterialCamera(this).stillShot();
        camera.saveDir(getAlbumStorageDir());
        camera.start(CAMERA_RQ);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_plant;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                imagePath = data.getDataString().replace("file:", "");
                ImageUtil.saveImage(imagePath);
                Drawable d = Drawable.createFromPath(imagePath);
                addImageButton.setImageDrawable(d);
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
                    camera.start(CAMERA_RQ);
                    return;
                }
                addImageWrapper.setVisibility(View.GONE);
                return;
            }
        }
    }

    public File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "plant_images");
        if (!file.mkdirs()) {
            Log.i("INFO", "Directory not created");
        }
        return file;
    }

    public void savePlant(View view) {
        db.insertPlant(plantName.getText().toString(), new SimpleDateFormat("MM/dd/yyyy").format(setDate), imagePath);
        goToPlantList();
    }
}

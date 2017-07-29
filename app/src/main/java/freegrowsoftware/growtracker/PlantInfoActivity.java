package freegrowsoftware.growtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;

import freegrowsoftware.growtracker.Util.LogEntryUtil;
import freegrowsoftware.growtracker.adapters.LogEntryAdapter;
import freegrowsoftware.growtracker.objects.LogEntry;
import freegrowsoftware.growtracker.objects.Plant;

public class PlantInfoActivity extends BaseActivity {
    Plant currentPlant;
    private ArrayList<LogEntry> logEntries;
    private LogEntryAdapter adapter;

    ListView logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data.containsKey("plant")) {
            currentPlant = (Plant) data.getSerializable("plant");
            setTitle(currentPlant.getName());
        }

        result = null;
        result = new DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar)
        .inflateMenu(R.menu.menu)
        .build();

        logEntries = LogEntryUtil.getAllLogEntries(getApplicationContext(), Integer.toString(currentPlant.getId()));

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarClickBackButton(PlantListActivity.class, null);

        logList = (ListView) findViewById(R.id.logDataList);
        adapter = new LogEntryAdapter(getApplicationContext(), R.layout.log_entry_adapter, logEntries);
        logList.setAdapter(adapter);
        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg) {
                LogEntry clickedLog = (LogEntry) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AddLogEntryActivity.class);
                intent.putExtra("log_image", clickedLog.getImageLocation());
                intent.putExtra("log_notes", clickedLog.getNotes());
                intent.putExtra("log_desc", clickedLog.getDescription());
                intent.putExtra("log_id", clickedLog.getId());
                intent.putExtra("plant", currentPlant);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_plant_info;
    }

    public void addLogEntry(View view) {
        goToCreateLogEntry();
    }

    @Override
    public void goToCreateLogEntry() {
        Intent intent = new Intent(getApplicationContext(), AddLogEntryActivity.class);
        intent.putExtra("plant", currentPlant);
        startActivity(intent);
        finish();
    }
}

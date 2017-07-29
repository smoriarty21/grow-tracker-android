package freegrowsoftware.growtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import freegrowsoftware.growtracker.adapters.PlantListAdapter;
import freegrowsoftware.growtracker.database.DBHelper;
import freegrowsoftware.growtracker.objects.Plant;

public class PlantListActivity extends BaseActivity {
    private DBHelper db;

    private ListView plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Plant List");

        db = new DBHelper(this);

        ArrayList<Plant> allPlants = db.getAllPlants();
        plantList = (ListView) findViewById(R.id.plant_list);
        PlantListAdapter plantAdapter = new PlantListAdapter(getApplicationContext(), R.layout.plant_list_item, allPlants);
        plantList.setAdapter(plantAdapter);

        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg) {
                Plant clickedPlant = (Plant) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), PlantInfoActivity.class);
                intent.putExtra("plant", clickedPlant);
                startActivity(intent);
                finish();
            }
        });

        //result.removeHeader();
        toolbar.removeViewAt(1);
        result = null;
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
        return R.layout.activity_plant_list;
    }

    public void addPlant(View view) {
        goToAddPlant();
    }
}

package freegrowsoftware.growtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {
    public Drawer result = null;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        resetMenu();
    }

    protected abstract int getLayoutResourceId();

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void goToAddPlant() {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToPlantList() {
        Intent intent = new Intent(this, PlantListActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToCreateLogEntry() {
        Intent intent = new Intent(this, AddLogEntryActivity.class);
        startActivity(intent);
        finish();
    }


    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setToolbarClickBackButton(final Class<? extends Activity> activityToOpen, final Map<String, Serializable> params) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activityToOpen);
                if (params != null) {
                    for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                        String key = entry.getKey();
                        Serializable value = entry.getValue();
                        intent.putExtra(key, value);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void resetMenu() {
        toolbar = null;
        result = null;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        result = new DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar)
        .inflateMenu(R.menu.menu)
        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (position) {
                    case 0:
                        goToPlantList();
                        break;
                    case 1:
                        goToAddPlant();
                        break;
                }

                return false;
            }
        })
        .build();
    }
}

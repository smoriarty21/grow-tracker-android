package freegrowsoftware.growtracker.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import freegrowsoftware.growtracker.R;
import freegrowsoftware.growtracker.objects.Plant;

public class PlantListAdapter extends ArrayAdapter<Plant> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Plant> plants = new ArrayList<Plant>();
    private int mResource;

    public PlantListAdapter(Context context, int resource, ArrayList<Plant> list) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        plants = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public Plant getItem(int position) {
        return plants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Plant plant = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }
        TextView plantName = (TextView) convertView.findViewById(R.id.plant_name);
        plantName.setText(plant.getName());

        TextView plantStartDate = (TextView) convertView.findViewById(R.id.plant_start_date);
        plantStartDate.setText(plant.getStartDate());

        ImageView plantImage = (ImageView) convertView.findViewById(R.id.plant_image);
        Drawable d = Drawable.createFromPath(plant.getImageLocation());
        plantImage.setImageDrawable(d);

        return convertView;
    }


}
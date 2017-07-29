package freegrowsoftware.growtracker.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import freegrowsoftware.growtracker.R;
import freegrowsoftware.growtracker.objects.LogEntry;

public class LogEntryAdapter extends ArrayAdapter<LogEntry> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();
    private int mResource;

    private TextView date;
    private TextView desc;
    private ImageView entryImage;

    public LogEntryAdapter(Context context, int resource, ArrayList<LogEntry> list) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        logEntries = list;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return logEntries.size();
    }

    @Override
    public LogEntry getItem(int position) {
        return logEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }
        LogEntry entry = logEntries.get(position);

        date = (TextView) convertView.findViewById(R.id.log_info_date);
        date.setText(entry.getDate());

        desc = (TextView) convertView.findViewById(R.id.log_info_desc);
        desc.setText(entry.getDescription());

        if (entry.getImageLocation() != null && !entry.getImageLocation().equals("")) {
            entryImage = (ImageView) convertView.findViewById(R.id.entry_image);
            Drawable d = Drawable.createFromPath(entry.getImageLocation());
            entryImage.setImageDrawable(d);
        }

        return convertView;
    }
}

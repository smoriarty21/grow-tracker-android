package freegrowsoftware.growtracker.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;

import freegrowsoftware.growtracker.database.DBHelper;

public class LogEntry implements Serializable {
    private int id;
    private String date;
    private String desc;
    private String notes;
    private String imageLocation;
    private Context mContext;

    private DBHelper db;

    public LogEntry(Context context, String date, String desc, String notes, String image) {
        this.mContext = context;
        this.date = date;
        this.desc = desc;
        this.notes = notes;
        this.imageLocation = image;
    }

    public LogEntry(Context context, int id, String date, String desc, String notes, String image) {
        this.mContext = context;
        this.id = id;
        this.date = date;
        this.desc = desc;
        this.notes = notes;
        this.imageLocation = image;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getImageLocation() {
        return this.imageLocation;
    }

    public String getDate() {
        return this.date;
    }

    public int getId() {
        return this.id;
    }

    public void save(String plant_id) {
        if (db != null) {
            db = null;
        }
        db = new DBHelper(mContext);

        SQLiteDatabase database = db.getWritableDatabase();
        if (getId() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("plant_id", plant_id);
            contentValues.put("description", getDescription());
            contentValues.put("notes", getNotes());
            contentValues.put("date", getDate());
            contentValues.put("image", getImageLocation());
            this.id = (int) database.insert("log_entries", null, contentValues);
        } else {
            database.execSQL( "UPDATE log_entries set description = ?, notes = ?, date = ?, image = ? WHERE id = ?", new String [] { getDescription(), getNotes(), getDate(), getImageLocation(), String.valueOf(getId()) });
        }
        db.close();
    }
}

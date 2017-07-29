package freegrowsoftware.growtracker.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import freegrowsoftware.growtracker.database.DBHelper;
import freegrowsoftware.growtracker.database.LogEntryColumns;
import freegrowsoftware.growtracker.objects.LogEntry;

public class LogEntryUtil {
    public static ArrayList<LogEntry> getAllLogEntries(Context context, String plant_id) {
        DBHelper db = new DBHelper(context);
        ArrayList<LogEntry> entries = new ArrayList<LogEntry>();

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor res =  database.rawQuery( "SELECT id, plant_id, description, notes, date, image FROM log_entries WHERE plant_id = ?", new String [] { String.valueOf(plant_id) });
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            int id = res.getInt(res.getColumnIndex(LogEntryColumns.ID));
            String desc = res.getString(res.getColumnIndex(LogEntryColumns.DESCRIPTION));
            String notes = res.getString(res.getColumnIndex(LogEntryColumns.NOTES));
            String date = res.getString(res.getColumnIndex(LogEntryColumns.DATE));
            String image_location = res.getString(res.getColumnIndex(LogEntryColumns.IMAGE));
            LogEntry entry = new LogEntry(context, id, date, desc, notes, image_location);
            entries.add(entry);
            res.moveToNext();
        }
        return entries;
    }
}

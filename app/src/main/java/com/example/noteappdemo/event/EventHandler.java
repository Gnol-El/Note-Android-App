package com.example.noteappdemo.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EventHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calendar";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "event";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_CONTENT = "content";

    public EventHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_event_table = String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_TITLE, KEY_DATE, KEY_TIME, KEY_CONTENT);
        db.execSQL(create_event_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop_event_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_event_table);

        onCreate(db);
    }

    public void addEvent(Event e) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, e.getTitle());
        values.put(KEY_DATE, e.getDate());
        values.put(KEY_TIME, e.getTime());
        values.put(KEY_CONTENT, e.getContent());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Event e = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return e;
    }
    public List<Event> getAllEvents() {
        List<Event>  EventList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Event e = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            EventList.add(e);
            cursor.moveToNext();
        }
        return EventList;
    }
    public void updateEvent(Event e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, e.getTitle());
        values.put(KEY_DATE, e.getDate());
        values.put(KEY_TIME, e.getTime());
        values.put(KEY_CONTENT, e.getContent());

        System.out.println("updating, " + e.getId() + ", " + e.getTime());
        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(e.getId()) });
        db.close();
    }
    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
    public List<Event> getAllEventsByDate(String date) {
        List<Event>  EventList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE date = ? ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(date) });
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Event e = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            EventList.add(e);
            cursor.moveToNext();
        }
        return EventList;
    }
}

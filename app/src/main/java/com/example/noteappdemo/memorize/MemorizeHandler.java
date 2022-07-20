package com.example.noteappdemo.memorize;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MemorizeHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calendar";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "memorize";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";

    public MemorizeHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_reminder_table = String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_TITLE, KEY_CONTENT);
        db.execSQL(create_reminder_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop_reminder_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_reminder_table);

        onCreate(db);
    }

    public void addMemorize(Memorize m) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, m.getTitle());
        values.put(KEY_CONTENT, m.getContent());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Memorize getMemorize(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Memorize r = new Memorize(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return r;
    }
    public List<Memorize> getAllMemorize() {
        List<Memorize> memorizeList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Memorize r = new Memorize(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            memorizeList.add(r);
            cursor.moveToNext();
        }
        return memorizeList;
    }
    public void updateMemorize(Memorize m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, m.getTitle());
        values.put(KEY_CONTENT, m.getContent());

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(m.getId()) });
        db.close();
    }
    public void deleteMemorize(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}

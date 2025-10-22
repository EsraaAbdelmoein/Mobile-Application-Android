package com.example.m03_bounce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bounce.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sample_table";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "x REAL, " +
                "y REAL, " +
                "dx REAL, " +
                "dy REAL, " +
                "color INTEGER, " +
                "name TEXT)";
        db.execSQL(createTable);
        Log.d("DB", "onCreate executed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d("DB", "onUpgrade executed");
    }

    public void save(DataModel ball) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("x", ball.getModelX());
        values.put("y", ball.getModelY());
        values.put("dx", ball.getModelDX());
        values.put("dy", ball.getModelDY());
        values.put("color", ball.getColor());
        values.put("name", ball.getName());
        long id = db.insert(TABLE_NAME, null, values);
        Log.d("DB", "save() inserted id=" + id + " x=" + ball.getModelX() + " y=" + ball.getModelY() + " dx=" + ball.getModelDX() + " dy=" + ball.getModelDY() + " color=" + ball.getColor() + " name=" + ball.getName());
        db.close();
    }

    public List<DataModel> findAll() {
        List<DataModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT x,y,dx,dy,color,name FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                float x = cursor.getFloat(0);
                float y = cursor.getFloat(1);
                float dx = cursor.getFloat(2);
                float dy = cursor.getFloat(3);
                int color = cursor.getInt(4);
                String name = cursor.getString(5);
                list.add(new DataModel(x, y, dx, dy, color, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("DB", "findAll() count=" + list.size());
        return list;
    }
}

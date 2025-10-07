package com.codelab.basics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "DB_Name.db";

    private static final String TABLE_NAME = "sample_table";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_NUMBER = "number";
    private static final String COL_POWERLEVEL = "powerLevel";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_ACCESSCOUNT = "accessCount";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COL_NAME + " TEXT, " +
                    COL_NUMBER + " INTEGER, " +
                    COL_POWERLEVEL + " INTEGER, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_ACCESSCOUNT + " INTEGER DEFAULT 0" +
                    ")";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBClass", "Creating DB: " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        addDefaultRows(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBClass", "Upgrading DB -> " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    private void addDefaultRows(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Bulbasaur', 1, 50, 'A Grass/Poison Pokémon', 0)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Charmander', 2, 60, 'A Fire Pokémon', 0)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Squirtle', 3, 55, 'A Water Pokémon', 0)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Pikachu', 4, 70, 'An Electric Pokémon', 0)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Jigglypuff', 5, 45, 'A Fairy Pokémon that sings', 0)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COL_NAME + ", " + COL_NUMBER + ", " + COL_POWERLEVEL + ", " + COL_DESCRIPTION + ", " + COL_ACCESSCOUNT +
                ") VALUES ('Meowth', 6, 48, 'A Mischievous Pokémon', 0)");
    }

    @Override
    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    @Override
    public int save(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, dataModel.getName());
        values.put(COL_NUMBER, dataModel.getNumber());
        values.put(COL_POWERLEVEL, dataModel.getPowerLevel());
        values.put(COL_DESCRIPTION, dataModel.getDescription());
        values.put(COL_ACCESSCOUNT, dataModel.getAccessCount());
        db.insert(TABLE_NAME, null, values);
        db.close();
        return 0;
    }

    @Override
    public int update(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ACCESSCOUNT, dataModel.getAccessCount());
        int rows = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(dataModel.getId())});
        db.close();
        return rows;
    }

    @Override
    public int deleteById(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                DataModel item = new DataModel(
                        cursor.getLong(0),   // id
                        cursor.getInt(2),    // number
                        cursor.getString(1), // name
                        cursor.getInt(3),    // powerLevel
                        cursor.getString(4), // description
                        cursor.getInt(5)     // accessCount
                );
                temp.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return temp;
    }

    @Override
    public String getNameById(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_NAME + " FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        String name = null;
        if (cursor.moveToFirst()) name = cursor.getString(0);
        cursor.close();
        db.close();
        return name;
    }

    @Override
    public DataModel getMax() {
        SQLiteDatabase db = this.getReadableDatabase();
        DataModel fav = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ACCESSCOUNT + " DESC LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {
            fav = new DataModel(
                    cursor.getLong(0),   // id
                    cursor.getInt(2),    // number
                    cursor.getString(1), // name
                    cursor.getInt(3),    // powerLevel
                    cursor.getString(4), // description
                    cursor.getInt(5)     // accessCount
            );
        }

        cursor.close();
        db.close();
        return fav;
    }

    @Override
    public void incAccessCount(long id) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET " + COL_ACCESSCOUNT + " = " + COL_ACCESSCOUNT + " + 1" +
                " WHERE " + COL_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    @Override
    public long getMostAccessed() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_ID + " FROM " + TABLE_NAME +
                        " ORDER BY " + COL_ACCESSCOUNT + " DESC LIMIT 1",
                null
        );
        long id = -1;
        if (cursor.moveToFirst()) id = cursor.getLong(0);
        cursor.close();
        db.close();
        return id;
    }
}

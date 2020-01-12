package com.example.foodJournal.database_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.database_model.WaterHistorySchema.*;

public class WaterHistoryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WATERHISTORY"+ InMemoryStorage.getUsername() +".db";
    public static final int DATABASE_VERSION =1;

    public WaterHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //so we don't need to worry about if the table exists, we just need to do insertion and query
    //outside
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WaterHistoryLIST_TABLE = "CREATE TABLE " +
                WaterHistoryEntry.TABLE_NAME + " (" +
                WaterHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WaterHistoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                WaterHistoryEntry.COLUMN_AMOUMT + " INTEGER NOT NULL, " +
                WaterHistoryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";
        db.execSQL(SQL_CREATE_WaterHistoryLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WaterHistoryEntry.TABLE_NAME);
    }
}

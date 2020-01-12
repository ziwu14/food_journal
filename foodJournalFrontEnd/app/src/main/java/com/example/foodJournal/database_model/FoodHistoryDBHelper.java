package com.example.foodJournal.database_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.database_model.FoodHistorySchema.*;

public class FoodHistoryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FOODHISTORY"+ InMemoryStorage.getUsername()+".db";
    public static final int DATABASE_VERSION =1;

    public FoodHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //so we don't need to worry about if the table exists, we just need to do insertion and query
    //outside
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FoodHistoryLIST_TABLE = "CREATE TABLE " +
                FoodHistoryEntry.TABLE_NAME + " (" +
                FoodHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodHistoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FoodHistoryEntry.COLUMN_AMOUMT + " FLOAT NOT NULL, " +
                FoodHistoryEntry.COLUMN_CALORIE + " FLOAT NOT NULL, " +
                FoodHistoryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";
        db.execSQL(SQL_CREATE_FoodHistoryLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FoodHistoryEntry.TABLE_NAME);
    }
}

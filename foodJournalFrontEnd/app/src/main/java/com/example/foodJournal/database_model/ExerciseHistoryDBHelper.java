package com.example.foodJournal.database_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.database_model.ExerciseHistorySchema.*;

public class ExerciseHistoryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "EXERCISEHISTORY"+ InMemoryStorage.getUsername() +".db";
    public static final int DATABASE_VERSION =1;

    public ExerciseHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //so we don't need to worry about if the table exists, we just need to do insertion and query
    //outside
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ExerciseHistoryLIST_TABLE = "CREATE TABLE " +
                ExerciseHistoryEntry.TABLE_NAME + " (" +
                ExerciseHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExerciseHistoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_AMOUMT + " INTEGER NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_CALORIE + " INTEGER NOT NULL, " +
                ExerciseHistoryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";
        db.execSQL(SQL_CREATE_ExerciseHistoryLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseHistoryEntry.TABLE_NAME);
    }
}

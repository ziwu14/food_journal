package com.example.foodJournal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodJournal.database_model.ExerciseHistoryDBHelper;
import com.example.foodJournal.database_model.ExerciseHistorySchema;
import com.example.foodJournal.database_model.FoodHistoryDBHelper;
import com.example.foodJournal.database_model.FoodHistorySchema;
import com.example.foodJournal.database_model.WaterHistoryDBHelper;
import com.example.foodJournal.database_model.WaterHistorySchema;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context,"Hello Javatpoint",Toast.LENGTH_SHORT).show();

        FoodHistoryDBHelper foodHistoryDBHelper = new FoodHistoryDBHelper(context);
        SQLiteDatabase foodDB = foodHistoryDBHelper.getWritableDatabase();
        foodDB.delete(FoodHistorySchema.FoodHistoryEntry.TABLE_NAME, null,null);


        WaterHistoryDBHelper waterHistoryDBHelper = new WaterHistoryDBHelper(context);
        SQLiteDatabase waterDB = waterHistoryDBHelper.getWritableDatabase();
        waterDB.delete(WaterHistorySchema.WaterHistoryEntry.TABLE_NAME, null, null);


        ExerciseHistoryDBHelper exerciseHistoryDBHelper = new ExerciseHistoryDBHelper(context);
        SQLiteDatabase exerciseDB = exerciseHistoryDBHelper.getWritableDatabase();
        exerciseDB.delete(ExerciseHistorySchema.ExerciseHistoryEntry.TABLE_NAME, null, null);
//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());
    }
}

package com.example.foodJournal.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.foodJournal.R;
import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.database_model.ExerciseHistoryDBHelper;
import com.example.foodJournal.database_model.ExerciseHistorySchema;
import com.example.foodJournal.database_model.FoodHistoryDBHelper;
import com.example.foodJournal.database_model.FoodHistorySchema;
import com.example.foodJournal.database_model.WaterHistoryDBHelper;
import com.example.foodJournal.database_model.WaterHistorySchema;

import java.util.Calendar;

public class MainPageActivity extends AppCompatActivity {

    private Button waterTarget;
    private Button caloriesTarget;
//    private TextView waterIntake;
//    private TextView caloriesIntake;
//    private TextView caloriesBurning;
//    private TextView caloriesBalance;

    private Button personalProfile;
    private Button foodManagement;
    private Button waterHistory;
    private Button exerciseHistory;
    private Button foodRecipe;

    private PendingIntent pendingIntent;
    //private Search
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setAlarm();
        setAllTextViews();
        setAllButtons();

    }

    private void setAlarm() {

        Intent intent = new Intent(this,
                AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                this, 0, intent, 0);

        /**
         * If the stated trigger time is in the past, the alarm will be triggered immediately,
         * with an alarm count depending on how far in the past the trigger time is relative to the repeat interval.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Calendar now = Calendar.getInstance();

        if(calendar.before(now)) {
            // handle this as you wish

            // adding a day to calendar
            calendar.add(Calendar.DATE, 1);
        }
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);


    }

    private void setAllTextViews(){

        caloriesTarget = (Button) findViewById(R.id.activity_main_page_caloriesDailyBound);
        waterTarget = (Button) findViewById(R.id.activity_main_page_waterTarget);

        try{
            int goal = InMemoryStorage.getWaterTimeLapse();
            Double intake = (double )do_get_all_water_intake();
            if (InMemoryStorage.getWaterTimeLapse_str() != null) {
                waterTarget.setText(String.valueOf(goal - intake.intValue())+"/" + goal+"onces");
            }

            Double goal_inDouble = InMemoryStorage.getFoodTimeLapse()*1.2;
            Double intake_inDouble = (double )do_get_all_calories_intake();
            Double burnt = (double) do_get_all_exercise_intake();
            //use the Harris-Benedict Equation to find daily calories
            //https://www.youtube.com/watch?v=uL19zPe_mGo
            if (InMemoryStorage.getFoodTimeLapse_str() != null) {
                caloriesTarget.setText(String.valueOf(goal_inDouble.intValue()-intake_inDouble.intValue()+burnt.intValue())+"/"+String.valueOf(goal_inDouble.intValue()) + "kcal");
            }
        } catch(Exception e) {

        }


        caloriesTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double intake = (double )do_get_all_calories_intake();
                double burnt = (double) do_get_all_exercise_intake();

                AlertDialog.Builder builder_calorie = new AlertDialog.Builder(MainPageActivity.this);
                builder_calorie.setCancelable(true);
                builder_calorie.setTitle("Daily Calorie Detail");
                builder_calorie.setMessage(
                        "Intake: " + String.valueOf(intake) + "\n" +
                        "Burnt: -" + String.valueOf(burnt));
                builder_calorie.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog_calorie = builder_calorie.create();
                dialog_calorie.show();
            }
        });

        waterTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double intake = (double )do_get_all_water_intake();

                AlertDialog.Builder builder_water = new AlertDialog.Builder(MainPageActivity.this);
                builder_water.setCancelable(true);
                builder_water.setTitle("Daily Water Detail");
                builder_water.setMessage(
                        "Intake: -" + String.valueOf(intake) + "\n");
                builder_water.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog_water = builder_water.create();
                dialog_water.show();

            }
        });
//        caloriesIntake.setText(String.valueOf(do_get_all_calories_intake()) + "/kcal");
//        caloriesBurning.setText("-" + String.valueOf(do_get_all_exercise_intake()) + "/kcal");
//        waterIntake.setText(String.valueOf(do_get_all_water_intake()) + "/onces");
    }
    
    private float do_get_all_calories_intake() {
        FoodHistoryDBHelper foodHistoryDBHelper = new FoodHistoryDBHelper(this);
        SQLiteDatabase mFoodDatabase = foodHistoryDBHelper.getWritableDatabase();
        float dailyCaloriesIntake = 0;
        Cursor cursor = mFoodDatabase.query(
                FoodHistorySchema.FoodHistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                float calorie = cursor.getFloat(
                        cursor.getColumnIndex(FoodHistorySchema.FoodHistoryEntry.COLUMN_CALORIE));
                dailyCaloriesIntake = dailyCaloriesIntake + calorie;
            }while(cursor.moveToNext());
        } else {

        }
        return dailyCaloriesIntake;
    }

    private float do_get_all_water_intake() {
        WaterHistoryDBHelper waterHistoryDBHelper = new WaterHistoryDBHelper(this);
        SQLiteDatabase mWaterDatabase = waterHistoryDBHelper.getWritableDatabase();
        float dailyWaterIntake = 0;
        Cursor cursor = mWaterDatabase.query(
                WaterHistorySchema.WaterHistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                float water = cursor.getFloat(
                        cursor.getColumnIndex(WaterHistorySchema.WaterHistoryEntry.COLUMN_AMOUMT));
                dailyWaterIntake = dailyWaterIntake + water;
            }while(cursor.moveToNext());
        } else {

        }
        return dailyWaterIntake;
    }

    private float do_get_all_exercise_intake() {
        ExerciseHistoryDBHelper exerciseHistoryDBHelper = new ExerciseHistoryDBHelper(this);
        SQLiteDatabase mExerciseDatabase = exerciseHistoryDBHelper.getWritableDatabase();
        float dailyExerciseIntake = 0;
        Cursor cursor = mExerciseDatabase.query(
                ExerciseHistorySchema.ExerciseHistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do{
                float exercise = cursor.getFloat(
                        cursor.getColumnIndex(ExerciseHistorySchema.ExerciseHistoryEntry.COLUMN_CALORIE));
                dailyExerciseIntake = dailyExerciseIntake + exercise;
            }while(cursor.moveToNext());
        } else {

        }
        return dailyExerciseIntake;
    }
    

    private void setAllButtons() {
        personalProfile = (Button) findViewById(R.id.activity_main_page_personal_profileBtn);
        personalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
                Intent intent = new Intent(MainPageActivity.this, PersonalProfileActivity.class);
                startActivity(intent);
            }
        });

        foodManagement = (Button) findViewById(R.id.activity_main_page_food_managementBtn);
        foodManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
                Intent intent = new Intent(MainPageActivity.this, FoodManagementActivity.class);
                startActivity(intent);
            }
        });

        exerciseHistory = (Button) findViewById(R.id.activity_main_page_exercise_historyBtn);
        exerciseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
                Intent intent = new Intent(MainPageActivity.this, ExerciseHistoryActivity.class);
                startActivity(intent);
            }
        });

        waterHistory = (Button) findViewById(R.id.activity_main_page_water_historyBtn);
        waterHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
                Intent intent = new Intent(MainPageActivity.this, WaterHistoryActivity.class);
                startActivity(intent);
            }
        });

        foodRecipe = (Button) findViewById(R.id.activity_main_page_food_recipeBtn);
        foodRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, FoodRecipeActivity.class);
                startActivity(intent);
            }
        });

    }
}

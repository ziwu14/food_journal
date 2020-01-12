package com.example.foodJournal.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodJournal.R;
import com.example.foodJournal.data.CONSTANT;
import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.database_model.FoodHistoryDBHelper;
import com.example.foodJournal.database_model.FoodHistorySchema;
import com.example.foodJournal.db_extractor.FoodList;
import com.example.foodJournal.db_extractor.USDA_Nutrition_Extraction;
import com.example.foodJournal.gson_model.DangerFoodModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * Overview:
 *
 * Creates the layout for the nutrition intent
 * Displays the nutrition extracted and prints it onto the screen
 * Has a back and add button
 *    -> back button brings the intent back to the FoodManagementActivity intent (search and display food item screen)
 *    -> add button adds the nutrition into ___ intent
 */
public class NutritionDetailActivity extends AppCompatActivity {

    public static TextView nutrition_food_name;
    public static TextView nutrition_calories;
    public static TextView nutrition_protein;
    public static TextView nutrition_sodium;
    public static TextView nutrition_fat;

    public static Context context;
    public static String ndbno;

    private SQLiteDatabase mDatabase;
    private EditText mAmount;

    private ArrayList<String[]> dangerList = new ArrayList<>();

    private final String getDangerFoodUrl_complete = CONSTANT.getDangerFoodUrl_incomplete + InMemoryStorage.getUsername();

    private Button back_button;
    private Button add_button;
    private Button danger_button;

    /**
     * Creates a TextView with nutritions on it
     * Creates a add button
     * Creates a back button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_detail);

        context = this;
        nutrition_food_name = (TextView) findViewById(R.id.nutrition_food_name);
        nutrition_calories = (TextView) findViewById(R.id.nutrition_food_calories);
        nutrition_protein = (TextView) findViewById(R.id.nutrition_food_protein);
        nutrition_sodium = (TextView) findViewById(R.id.nutrition_food_sodium);
        nutrition_fat = (TextView) findViewById(R.id.nutrition_food_fat);

        /**
         * Creates an USDA_Nutrition_Extraction class to retrieve nutrition data in a separate thread
         */

        final USDA_Nutrition_Extraction usda = new USDA_Nutrition_Extraction(ndbno);
        usda.execute();//AsyncTask built-in function to copy info from search thread to main thread
        //info is in usda now, we could use getter

        /**
         * Initiates back button to return back to the search intent
         */

        FoodHistoryDBHelper dbHelper = new FoodHistoryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mAmount = (EditText) findViewById(R.id.nutrition_add_amount);

        back_button = (Button) findViewById(R.id.nutrition_backBtn);
        add_button = (Button) findViewById(R.id.nutrition_addBtn);
        danger_button = (Button) findViewById(R.id.nutrition_dangerBtn);

        if (InMemoryStorage.getUsername() == null) {
            mAmount.setVisibility(View.GONE);
            add_button.setVisibility(View.GONE);
            back_button.setVisibility(View.GONE);
            danger_button.setVisibility(View.GONE);
        } else {

            back_button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodManagementActivity.context, FoodManagementActivity.class);
                    FoodManagementActivity.searchbar.setText(FoodManagementActivity.search);
                    FoodManagementActivity.food_spinner.setSelection(FoodManagementActivity.spinner_pos);
                    FoodManagementActivity.enter_button.performClick();
                    FoodManagementActivity.scrollView.setScrollY(FoodManagementActivity.scroll_pos);
                    startActivity(intent);
                }
            });

            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2.check unit_calorie
                    final String unit_calorie = nutrition_calories.getText().toString().replaceAll("\\D+","");
                    if (unit_calorie.equals("")) {
                        Toast.makeText(getApplicationContext(),"No energy information available " +
                                "for this food, so you can't add it to the history",Toast.LENGTH_SHORT).show();
                    } else {
                        do_receive_danger_food_from_server(unit_calorie);
                    }
                }
            });

            danger_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    do_upload_to_server();
                }
            });
        }

    }


    public void do_receive_danger_food_from_server(final String unit_calorie) {
        OkHttpClient client = new OkHttpClient();
        Request newRequest = new Request.Builder()
                .url(getDangerFoodUrl_complete)
                .build();

        client.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RECEIVE DANGER FOOD", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json_string = response.body().string();
                    NutritionDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (json_string != null){
                                //https://www.youtube.com/watch?v=ZZddxpxGQPE&list=PLpUMhvC6l7AOy4UEORSutzFus98n-Es_l&index=3
                                Gson gson = new Gson();
                                DangerFoodModel[] foodList = gson.fromJson(json_string, DangerFoodModel[].class);
                                for (int i = 0; i < foodList.length; i ++) {
                                    String[] temp = new String[] {foodList[i].getName()};
                                    dangerList.add(temp);
                                }

                                do_check_and_add_to_food_history(unit_calorie);
                            }
                        }
                    });
                }
            }
        });
    }

    private void do_check_and_add_to_food_history(final String unit_calorie) {
        final String name = FoodList.current_food;
        Boolean isDangerFood = false;

        for(int i = 0; i < dangerList.size(); i++) {
            if (name.equals(dangerList.get(i)[0])) {
                isDangerFood = true;
                break;
            }
        }

        if (isDangerFood) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("Warning");
            builder.setMessage("This is danger food");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            do_add_to_food_history(unit_calorie);
                            Intent intent = new Intent(NutritionDetailActivity.context, FoodHistoryActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            do_add_to_food_history(unit_calorie);
            Intent intent = new Intent(NutritionDetailActivity.context, FoodHistoryActivity.class);
            startActivity(intent);
        }
    }

    private void do_add_to_food_history(final String unit_calorie) {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        float amount = Float.valueOf(mAmount.getText().toString());
        float calories = amount * Float.valueOf(unit_calorie)/100;
        String amount_str = String.valueOf(amount);
        String calories_str = String.valueOf(calories);
        ContentValues cv = new ContentValues();
        cv.put(FoodHistorySchema.FoodHistoryEntry.COLUMN_NAME, FoodList.current_food);
        cv.put(FoodHistorySchema.FoodHistoryEntry.COLUMN_AMOUMT, amount_str);
        cv.put(FoodHistorySchema.FoodHistoryEntry.COLUMN_CALORIE, calories_str);
        cv.put(FoodHistorySchema.FoodHistoryEntry.COLUMN_TIMESTAMP, timestamp);
        mDatabase.insert(FoodHistorySchema.FoodHistoryEntry.TABLE_NAME, null, cv);

//        try {
//            Cursor cursor = mDatabase.query(
//                    FoodHistorySchema.FoodHistoryEntry.TABLE_NAME,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    FoodHistorySchema.FoodHistoryEntry.COLUMN_TIMESTAMP + " DESC"
//            );
//
//            if (cursor.getCount() != 0 ) {
//                cursor.moveToFirst();
//                do{
//                    String name = cursor.getString(
//                            cursor.getColumnIndex(WaterHistorySchema.WaterHistoryEntry.COLUMN_NAME));
//                    Log.d("add", name);
//                }while(cursor.moveToNext());
//            } else {
//                Log.d("add", "empty cursor");
//            }
//        } catch (Exception e) {
//            Log.d("add", "can't do query ");
//        }


    }

    private void do_upload_to_server() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!check what is the data type in the database !!!!!!!!!!!!!!
                .add("name", FoodList.current_food)
                .build();

        Request newRequest = new Request.Builder()
                .url(getDangerFoodUrl_complete)
                .post(body)
                .build();

        client.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("UPLOAD DANGER FOOD", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Intent intent =  new Intent(NutritionDetailActivity.this, DangerFoodActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}



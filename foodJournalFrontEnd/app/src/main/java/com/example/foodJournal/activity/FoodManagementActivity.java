package com.example.foodJournal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodJournal.R;
import com.example.foodJournal.db_extractor.FoodItem;
import com.example.foodJournal.db_extractor.FoodList;

import java.util.ArrayList;
import java.util.List;

/**
 * Overview:
 *
 * Main food search and display screen
 * Briefly organizes the features within the screen
 *    -> Spinner
 *    -> Enter button
 *    -> Search bar
 * Contains the two arrays that has corresponding food group names and id ndbno
 */

public class FoodManagementActivity extends AppCompatActivity {

    /**
     * These public static field variables are accessed by the class FoodList
     * Used in another thread so that it will not affect the main thread (this)
     */

    public static TextView text;
    public static LinearLayout ll;
    public static Button but;
    public static Context context;
    public static List<FoodItem> food_list = new ArrayList<>();
    public static ImageButton enter_button;
    public static Spinner food_spinner;
    public static EditText searchbar;
    public static String search, food_ground;
    public static int spinner_pos = 0, scroll_pos = 0;
    public static ScrollView scrollView;
    public static SharedPreferences.Editor saved_food;
    public static SharedPreferences prefs;

    /**
     * Information regarding the food ground and the corresponding id ndbno
     */


    private String[] group_array = {"All", "American Indian/Alaska Native Foods", "Baby Foods",
            "Baked Products", "Beef Products", "Beverages", "Breakfast Cereals", "Cereal Grains and Pasta",
            "Dairy and Egg Products", "Fast Foods", "Fats and Oils", "Finfish and Shellfish Products",
            "Fruits and Fruit Juices", "Lamb, Veal, and Game Products", "Legumes and Legume Products",
            "Meals, Entrees, and Side Dishes", "Nut and Seed Products", "Pork Products", "Poultry Products",
            "Restaurant Foods", "Sausages and Luncheon Meats", "Snacks", "Soups, Sauces, and Gravies", "Spices and Herbs",
            "Sweets", "Vegetables and Vegetable Products"};

    private String[] id = {"", "3500", "0300", "1800", "1300", "1400", "0800", "2000", "0100", "2100", "0400",
            "1500", "0900", "1700", "1600", "2200", "1200", "1000", "0500", "3600", "0700", "2500", "0600",
            "0200", "1900", "1100"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_management);


        /**
         * Initiates the drop down spinner with the food group item *
         */
        food_spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_spinner.setAdapter(adapter);

        /**
         * Initiates the scrollview
         */

        ll = (LinearLayout) findViewById(R.id.scrolllayout);
        scrollView = (ScrollView)findViewById(R.id.scroll);



        /**
         * Initiates the enter button
         * Opens a new intent through the class FoodList with the given value from
         *     food group and text entered into the search bar
         */
        enter_button = (ImageButton) findViewById(R.id.enterbutton);
        context = this;
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = searchbar.getText().toString();
                spinner_pos = food_spinner.getSelectedItemPosition();
                food_ground = id[food_spinner.getSelectedItemPosition()];

                scroll_pos = scrollView.getScrollY();

                FoodList food_list_class = new FoodList(search, food_ground);
                food_list_class.execute();

                searchbar.setText("");
            }
        });


        /**
         * Initiates the search bar
         * When entered, it performs a click on the enter button
         */
        searchbar = (EditText) findViewById(R.id.searchbar);
        searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    enter_button.performClick();
                }
                return false;
            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.activity_food_management_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodManagementActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

        ImageButton historyBtn = (ImageButton) findViewById(R.id.activity_food_management_historyBtn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodManagementActivity.this, FoodHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

}
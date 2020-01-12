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
import com.example.foodJournal.db_extractor.RecipeItem;
import com.example.foodJournal.db_extractor.RecipeList;

import java.util.ArrayList;
import java.util.List;

public class GuestActivity extends AppCompatActivity {

    //----------------------scrollview--------------------------
    public static LinearLayout ll;
    public static Button but;
    public static ScrollView scrollView;
    public static Context context;
    public static int spinner_pos = 0, scroll_pos = 0;
    //---------------------food-----------------------------------
    public static TextView food_text;
    public static List<FoodItem> food_list = new ArrayList<>();
    public static ImageButton food_enter_button;
    public static Spinner food_spinner;
    public static EditText food_searchbar;
    public static String food_search, food_ground;


    public static SharedPreferences.Editor saved_food;
    public static SharedPreferences prefs;
    //---------------------recipe--------------------------------
    public static TextView recipe_text;

    public static List<RecipeItem> recipe_list = new ArrayList<>();
    public static ImageButton recipe_enter_button;
    public static EditText recipe_searchbar;
    public static String recipe_search;

    //--------------------name constant and id------------------
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
        setContentView(R.layout.activity_guest);

        ll = (LinearLayout) findViewById(R.id.scrolllayout_guest);
        scrollView = (ScrollView)findViewById(R.id.scroll_guest);

        //---------------------------------------------------------food----------------------------------------------------------
        /**
         * Initiates the drop down spinner with the food group item *
         */
        food_spinner = (Spinner) findViewById(R.id.spinner_guest);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_spinner.setAdapter(adapter);


        food_enter_button = (ImageButton) findViewById(R.id.enterbutton_guest_food);
        context = this;
        food_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_search = food_searchbar.getText().toString();
                spinner_pos = food_spinner.getSelectedItemPosition();
                food_ground = id[food_spinner.getSelectedItemPosition()];

                scroll_pos = scrollView.getScrollY();

                FoodList food_list_class = new FoodList(food_search, food_ground);
                food_list_class.execute();

                food_searchbar.setText("");
            }
        });


        /**
         * Initiates the search bar
         * When entered, it performs a click on the enter button
         */
        food_searchbar = (EditText) findViewById(R.id.searchbar_guest_food);
        food_searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    food_enter_button.performClick();
                }
                return false;
            }
        });
        //----------------------------------------------recipe-------------------------------------------
        recipe_enter_button = (ImageButton) findViewById(R.id.enterbutton_guest_recipe);
        recipe_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                recipe_search = recipe_searchbar.getText().toString();
                scroll_pos = scrollView.getScrollY();

                RecipeList recipe_list_class = new RecipeList(recipe_search);
                recipe_list_class.execute();

                recipe_searchbar.setText("");
            }
        });


        recipe_searchbar = (EditText) findViewById(R.id.searchbar_guest_recipe);
        recipe_searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    recipe_enter_button.performClick();
                }
                return false;
            }
        });

        //---------------------------------------------back button---------------------------------------

        ImageButton backBtn = (ImageButton) findViewById(R.id.activity_guest_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

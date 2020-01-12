package com.example.foodJournal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.foodJournal.R;
import com.example.foodJournal.data.InMemoryStorage;
import com.example.foodJournal.db_extractor.RecipeItem;
import com.example.foodJournal.db_extractor.RecipeList;

import java.util.ArrayList;
import java.util.List;

public class FoodRecipeActivity extends AppCompatActivity {

    public static TextView text;
    public static LinearLayout ll;
    public static Button but;
    public static Context context;
    public static List<RecipeItem> recipe_list = new ArrayList<>();
    public static ImageButton enter_button;
    public static EditText searchbar;
    public static String search;
    public static int scroll_pos = 0;
    public static ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipe);


        ll = (LinearLayout) findViewById(R.id.scrolllayout_recipe);
        scrollView = (ScrollView)findViewById(R.id.scroll_recipe);


        enter_button = (ImageButton) findViewById(R.id.enterbutton_recipe);
        context = this;
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                search = searchbar.getText().toString();
                scroll_pos = scrollView.getScrollY();

                RecipeList recipe_list_class = new RecipeList(search);
                recipe_list_class.execute();

                searchbar.setText("");
            }
        });


        searchbar = (EditText) findViewById(R.id.searchbar_recipe);
        searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    enter_button.performClick();
                }
                return false;
            }
        });

        ImageButton backBtn = (ImageButton)  findViewById(R.id.activity_food_recipe_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InMemoryStorage.getUsername() != null) {
                    Intent intent = new Intent(FoodRecipeActivity.this, MainPageActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FoodRecipeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}

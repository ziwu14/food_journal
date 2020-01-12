package com.example.foodJournal.db_extractor;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.foodJournal.activity.FoodRecipeActivity;
import com.example.foodJournal.activity.GuestActivity;
import com.example.foodJournal.activity.WebViewActivity;
import com.example.foodJournal.data.CONSTANT;
import com.example.foodJournal.data.InMemoryStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RecipeList extends AsyncTask<Void,Void,Void> {

    private String API_KEY =CONSTANT.food2fork_api_key;
    private String searched_food;
    private static ArrayList<RecipeItem> recipe_list2;
    private static String current_recipe;// i.e. clicked one


    public RecipeList(String food) {
        searched_food = food;
    }

    /**
     * search in background
     */
    @Override
    protected Void doInBackground(Void... voids) {
        recipe_list2 = new ArrayList<RecipeItem>();
        String JsonResponse = "", name = "", url_src = "";
        URL url = null;

        try {
            url = new URL("https://www.food2fork.com/api/search?key="+API_KEY+"&q="+searched_food);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is));

            JsonResponse = read.readLine();
            while (JsonResponse.indexOf("publisher") != -1) {
                int index_f2f_url = JsonResponse.indexOf("f2f_url");
                if (index_f2f_url == -1) {
                    break;
                }
                int index_title = JsonResponse.indexOf("title");
                int index_source_url = JsonResponse.indexOf("source_url");

                url_src = JsonResponse.substring(index_f2f_url+11, index_title -4);
                name = JsonResponse.substring(index_title+9, index_source_url-4);
                recipe_list2.add(new RecipeItem(name,url_src));//add to container
                JsonResponse = JsonResponse.substring(index_source_url+9,JsonResponse.length() - 2);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        FoodRecipeActivity.recipe_list.clear();
        
        if (InMemoryStorage.getUsername() == null) {
            for (int i = 0; i < recipe_list2.size(); i++) {
                GuestActivity.recipe_list.add(recipe_list2.get(i));
            }

            GuestActivity.ll.removeAllViews();

            for(int i = 0; i< GuestActivity.recipe_list.size(); i++){
                GuestActivity.but = new Button(GuestActivity.context);
                GuestActivity.but.setText(GuestActivity.recipe_list.get(i).getName());
                GuestActivity.but.setId(i);
                GuestActivity.but.setBackgroundColor(Color.rgb(205,92,92));
                GuestActivity.but.setTextColor(Color.rgb(255,255,255));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,250);
                layoutParams.setMargins(60,5,60,5);
                GuestActivity.ll.addView(GuestActivity.but, layoutParams);
                GuestActivity.but.setOnClickListener(click(GuestActivity.recipe_list.get(GuestActivity.but.getId()).getUrl()));
            }
        } else {
            for (int i = 0; i < recipe_list2.size(); i++) {
                FoodRecipeActivity.recipe_list.add(recipe_list2.get(i));
            }

            FoodRecipeActivity.ll.removeAllViews();
            
            for(int i = 0; i< FoodRecipeActivity.recipe_list.size(); i++){
                FoodRecipeActivity.but = new Button(FoodRecipeActivity.context);
                FoodRecipeActivity.but.setText(FoodRecipeActivity.recipe_list.get(i).getName());
                FoodRecipeActivity.but.setId(i);
                FoodRecipeActivity.but.setBackgroundColor(Color.rgb(205,92,92));
                FoodRecipeActivity.but.setTextColor(Color.rgb(255,255,255));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,250);
                layoutParams.setMargins(60,5,60,5);
                FoodRecipeActivity.ll.addView(FoodRecipeActivity.but, layoutParams);
                FoodRecipeActivity.but.setOnClickListener(click(FoodRecipeActivity.recipe_list.get(FoodRecipeActivity.but.getId()).getUrl()));
            }
        }
        
        
    }

    /**
     * when click an item, redirect to the corresponding recipe url
     * this is called by the onPostExecute function above
     */
    public static View.OnClickListener click(final String url){
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InMemoryStorage.getUsername() == null) {
                    Intent intent = new Intent(GuestActivity.context, WebViewActivity.class);
                    intent.putExtra("recipeUrl",url);
                    GuestActivity.context.startActivity(intent);
                } else {
                    Intent intent = new Intent(FoodRecipeActivity.context, WebViewActivity.class);
                    intent.putExtra("recipeUrl",url);
                    FoodRecipeActivity.context.startActivity(intent);
                }

            }
        };
        return onclick;
    }
}

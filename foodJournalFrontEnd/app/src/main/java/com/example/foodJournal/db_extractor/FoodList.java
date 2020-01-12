package com.example.foodJournal.db_extractor;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.foodJournal.activity.FoodManagementActivity;
import com.example.foodJournal.activity.GuestActivity;
import com.example.foodJournal.activity.NutritionDetailActivity;
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

/*
-- container for food item {name, dbno.}
-- current food aka. clicked one
 */

public class FoodList extends AsyncTask<Void, Void, Void>{

    private String API_KEY = CONSTANT.USDA_api_key, food, foodGroup;
    private static ArrayList<FoodItem> food_list2;//store a list of foods
    public static String current_food;//the food we click on

    public FoodList(String search1, String foodGroup_){
        food = search1;//searched food
        foodGroup = foodGroup_;//i.e. food group
    }


    /**
     * search in background
     * Extracts the info by search API with parameters of food name, food ground, top 25 most relevant
     * Reads from an online JSON file and adds the food{name, dbno.} to the food list
     */
    @Override
    protected Void doInBackground(Void... Void) {

        food_list2 = new ArrayList<FoodItem>();
        String JsonResponse = "", name ="", ndbno = "";
        URL url = null;

        try {
            url = new URL("https://api.nal.usda.gov/ndb/search/?format=json&q="+food+"&fg="+ foodGroup +
                    "&sort=n&max=25&offset=0&api_key="+API_KEY);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is));

            while ((JsonResponse = read.readLine()) != null) {


                String temp = JsonResponse;
                if (JsonResponse.indexOf("name")!=-1) {
                    if (JsonResponse.indexOf("UPC")!=-1) {
                        temp = JsonResponse.substring(JsonResponse.indexOf("name")+8, JsonResponse.length()-1);
                        name = temp.substring(0,temp.indexOf("UPC")-2);//name
                    }
                    else if (JsonResponse.indexOf("UPC")==-1){
                        name = JsonResponse.substring(JsonResponse.indexOf("name")+8, JsonResponse.length()-2);
                    }
                }
                else if (JsonResponse.indexOf("ndbno")!=-1){
                    ndbno = JsonResponse.substring(JsonResponse.indexOf("ndbno")+9, JsonResponse.length()-2);
                }


                if (JsonResponse.indexOf("ndbno")!=-1 && !name.equals("") && !ndbno.equals("")) {
                    food_list2.add(new FoodItem(name,ndbno));//add to container
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Adds foods found from search thread to the main thread for UI *
     */
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        
        if (InMemoryStorage.getUsername() == null) {
            //Clears the static master arraylist from GuestActivity class
            GuestActivity.food_list.clear();

            //Copies the food items inside this thread's arraylist to GuestActivity class's arraylist
            for(int i = 0; i<food_list2.size(); i++){
                GuestActivity.food_list.add(food_list2.get(i));
            }

            //Removes every previous button in GuestActivity's linear layout
            try{
                GuestActivity.ll.removeAllViews();//clear the Linear layout
            } catch (Exception e) {

            }

            /**
             * Creates a button for each food item within the arraylist, each button in GuestActivity is a static type
             * Adds the OnClickListener to each button
             */
            for(int i = 0; i< GuestActivity.food_list.size(); i++){
                GuestActivity.but = new Button(GuestActivity.context);//the button is not binding to layout xml but will bind to Linear layout
                GuestActivity.but.setText(GuestActivity.food_list.get(i).getname());
                GuestActivity.but.setBackgroundColor(Color.rgb(205,92,92));
                GuestActivity.but.setTextColor(Color.rgb(255,255,255));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,250);
                layoutParams.setMargins(60,5,60,5);
                GuestActivity.but.setId(Integer.parseInt(GuestActivity.food_list.get(i).getndbno()));//setId() and getId()
                GuestActivity.ll.addView(GuestActivity.but,layoutParams);//add a new view to the ListView
                GuestActivity.but.setOnClickListener(click(GuestActivity.but.getId(), GuestActivity.food_list.get(i).getname()));
            }
        } else {
            //Clears the static master arraylist from FoodManagementActivity class
            try {
                FoodManagementActivity.food_list.clear();
            } catch (Exception e) {

            }

            //Copies the food items inside this thread's arraylist to FoodManagementActivity class's arraylist
            for(int i = 0; i<food_list2.size(); i++){
                FoodManagementActivity.food_list.add(food_list2.get(i));
            }

            //Removes every previous button in FoodManagementActivity's linear layout
            FoodManagementActivity.ll.removeAllViews();//clear the Linear layout

            /**
             * Creates a button for each food item within the arraylist, each button in FoodManagementActivity is a static type
             * Adds the OnClickListener to each button
             */
            for(int i = 0; i< FoodManagementActivity.food_list.size(); i++){
                FoodManagementActivity.but = new Button(FoodManagementActivity.context);//the button is not binding to layout xml but will bind to Linear layout
                FoodManagementActivity.but.setText(FoodManagementActivity.food_list.get(i).getname());
                FoodManagementActivity.but.setBackgroundColor(Color.rgb(205,92,92));
                FoodManagementActivity.but.setTextColor(Color.rgb(255,255,255));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,250);
                layoutParams.setMargins(60,5,60,5);
                FoodManagementActivity.but.setId(Integer.parseInt(FoodManagementActivity.food_list.get(i).getndbno()));//setId() and getId()
                FoodManagementActivity.ll.addView(FoodManagementActivity.but,layoutParams);//add a new view to the ListView
                FoodManagementActivity.but.setOnClickListener(click(FoodManagementActivity.but.getId(), FoodManagementActivity.food_list.get(i).getname()));
            }
        }
        
        
    }

    /**
     * activity redirection when click on the food
     * @param num the ndbno ndbno that corresponds to the food item id
     * @return the OnClickListener that opens a initiates the ndbno ndbno in the new intent and opens the new intent
     */

    public static View.OnClickListener click(final int num, final String name){
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            NutritionDetailActivity.ndbno = "" + num;
            current_food = name;
            if (InMemoryStorage.getUsername() == null) {
                Intent intent = new Intent(GuestActivity.context, NutritionDetailActivity.class);
                GuestActivity.context.startActivity(intent);
            } else {
                Intent intent = new Intent(FoodManagementActivity.context, NutritionDetailActivity.class);
                FoodManagementActivity.context.startActivity(intent);
            }

            }
        };
        return onclick;
    }
}
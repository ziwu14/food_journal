package com.example.foodJournal.db_extractor;


import android.os.AsyncTask;
import android.util.Log;

import com.example.foodJournal.activity.NutritionDetailActivity;
import com.example.foodJournal.data.CONSTANT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * DESCRIPTION
 * extracts the nutritional values via the reports API
 * Sets the TextView in NutritionDetailActivity
 * INPUT dbno.
 * OUTPUT text, water, colaries, protein, sodium, fat
 */
public class USDA_Nutrition_Extraction extends AsyncTask<Void,Void,Void>{

    private final String API_KEY= CONSTANT.USDA_api_key, ndbno;
    private String JsonResponse , calories, protein, sodium, fat;

    public USDA_Nutrition_Extraction(String number){
        ndbno = number;
    }

    /**
     * search in background
     */
    @Override
    protected Void doInBackground(Void... voids) {
        URL url;
        try {
            url = new URL("https://api.nal.usda.gov/ndb/V2/reports?ndbno="+ ndbno +"&type=f&format=json&api_key="+API_KEY);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is));
            JsonResponse = read.readLine();
            try{
                calories = get_nutr(JsonResponse,"Energy");
                protein = get_nutr(JsonResponse,"Protein");
                sodium = get_nutr(JsonResponse,"Sodium");
                fat = get_nutr(JsonResponse,"Total lipid (fat)");
            }catch (Exception e){
            }

        } catch (MalformedURLException e) {
            Log.d("EXTRACT", "malformedURL");
        } catch (IOException e) {
            Log.d("EXTRACT", "error when reading from URL");
        }
        return null;
    }

    /**
     * extract value based on the tag
     */

    public String get_nutr(String JsonResponse, String tag) {
        String unit="g", value="g", total = "No data";
        if (JsonResponse.indexOf(tag)!=-1) {
            JsonResponse = JsonResponse.substring(JsonResponse.indexOf(tag));

            String temp = JsonResponse.substring(JsonResponse.indexOf("unit"));
            unit = temp.substring(7,temp.indexOf(",")-1);

            temp = JsonResponse.substring(JsonResponse.indexOf("value"));
            value = temp.substring(7,temp.indexOf(",")-1);
            value = value.replace("\"", "");

            total = value+unit;
        }
        return total;
    }


    //--------------------------------------------we need to modify here--------------------------------------------------------
    /**
     * Sets the TextView in NutritionDetailActivity to the nutrition_text
     * @param aVoid
     */

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);

        NutritionDetailActivity.nutrition_food_name.append(FoodList.current_food+"(per 100 gram)");
        NutritionDetailActivity.nutrition_calories.append(calories);
        NutritionDetailActivity.nutrition_protein.append(protein);
        NutritionDetailActivity.nutrition_sodium.append(sodium);
        NutritionDetailActivity.nutrition_fat.append(fat);
    }


}
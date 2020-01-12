package com.example.foodJournal.data;

import android.util.Log;

public class InMemoryStorage {

    static String username = null;
    static String firstName = null;
    static String lastName = null;
    static String gender = null;
    static Integer age = null;
    static String age_str = null;
    static String weight = null;
    static String height = null;
    static String bmi = null;
    static Integer healthTarget = null;
    static String healthTarget_str = null;
    static Integer foodTimeLapse = null;
    static String foodTimeLapse_str = null;
    static Integer waterTimeLapse = null;
    static String waterTimeLapse_str = null;



    public static void setUsername(String username_) {
        username = username_;
    }
    public static void setHealthInfo(String firstName_,
                              String lastName_, String gender_, int age_,
                              String weight_, String height_, String bmi_,
                              int healthTarget_, int foodTimeLapse_, int waterTimeLapse_) {
        firstName = firstName_;
        lastName = lastName_;
        gender = gender_;
        age = age_;
        weight = weight_;
        height = height_;
        bmi = bmi_;
        healthTarget = healthTarget_;
        foodTimeLapse = foodTimeLapse_;
        waterTimeLapse = waterTimeLapse_;

        age_str = (age == null) ? "" : age.toString();
        healthTarget_str = (healthTarget == null) ? "" : healthTarget.toString();
        foodTimeLapse_str = (foodTimeLapse == null) ? "" : foodTimeLapse.toString();
        waterTimeLapse_str = (waterTimeLapse == null) ? "" : waterTimeLapse.toString();
    }
    static public void clearAll() {
        username = null;
        firstName = null;
        lastName = null;
        gender = null;
        age = null;
        age_str = null;
        weight = null;
        height = null;
        bmi = null;
        healthTarget = null;
        healthTarget_str = null;
        foodTimeLapse = null;
        foodTimeLapse_str = null;
        waterTimeLapse = null;
        waterTimeLapse_str = null;
    }
    static public void printAllInfos(){
        Log.d("LOCAL_STORAGE","print all local storage:");
        Log.d("LOCAL_STORAGE","firstName: "+firstName);
        Log.d("LOCAL_STORAGE","lastName: "+lastName);
        Log.d("LOCAL_STORAGE","gender: "+gender);
        Log.d("LOCAL_STORAGE","age: "+age_str);
        Log.d("LOCAL_STORAGE","weight: "+weight);
        Log.d("LOCAL_STORAGE","height: "+height);
        Log.d("LOCAL_STORAGE","bmi: "+bmi);
        Log.d("LOCAL_STORAGE","healthTarget: "+healthTarget_str);
        Log.d("LOCAL_STORAGE","foodTimeLapse: "+foodTimeLapse_str);
        Log.d("LOCAL_STORAGE","waterTimeLapse: "+waterTimeLapse_str);

    }

    public static String getUsername() {
        return username;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getGender() {
        return gender;
    }

    public static Integer getAge() {
        return age;
    }

    public static String getAge_str() {
        return age_str;
    }

    public static String getWeight() {
        return weight;
    }

    public static String getHeight() {
        return height;
    }

    public static String getBmi() {
        return bmi;
    }

    public static Integer getHealthTarget() {
        return healthTarget;
    }

    public static String getHealthTarget_str() {
        return healthTarget_str;
    }

    public static Integer getFoodTimeLapse() {
        return foodTimeLapse;
    }

    public static String getFoodTimeLapse_str() {
        return foodTimeLapse_str;
    }

    public static Integer getWaterTimeLapse() {
        return waterTimeLapse;
    }

    public static String getWaterTimeLapse_str() {
        return waterTimeLapse_str;
    }
}

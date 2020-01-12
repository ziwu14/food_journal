package com.example.foodJournal.gson_model;

public class DangerFoodModel {

    String model;
    int pk;
    DangerFoodFields fields;

    class DangerFoodFields {
        String account;
        String name;
        String foodID;
    };

    public String getName() {
        return fields.name;
    }

}

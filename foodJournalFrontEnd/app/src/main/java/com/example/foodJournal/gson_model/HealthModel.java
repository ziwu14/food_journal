package com.example.foodJournal.gson_model;

public class HealthModel {

    String model;
    int pk;
    HealthFields fields;

    private class HealthFields {
        String firstName;
        String lastName;
        String gender;
        int age;
        String weight;
        String height;
        String bmi;
        int healthTarget;
        int foodTimeLapse;
        int waterTimeLapse;

    };
    public String getFirstName() {
        return fields.firstName;
    }
    public String getLastName() {
        return fields.lastName;
    }
    public String getGender() {
        return fields.gender;
    }
    public Integer getAge() {
        return fields.age;
    }
    public String getWeight() {
        return fields.weight;
    }
    public String getHeight() {
        return fields.height;
    }
    public  String getBMI() {
        return fields.bmi;
    }
    public  Integer getHealthTarget() {
        return fields.healthTarget;
    }
    public Integer getFoodTimeLapse() {
        return fields.foodTimeLapse;
    }
    public Integer getWaterTimeLapse() {
        return fields.waterTimeLapse;
    }
}

/*
[
    {
        "model": "food.healthinfo",
        "pk": 36,
        "fields": {
            "firstName": "z",
            "lastName": "w",
            "gender": "male",
            "age": 50,
            "weight": "50.0",
            "height": "300.00",
            "bmi": "200.00",
            "healthTarget": 100,
            "foodTimeLapse": 50,
            "waterTimeLapse": 50
        }
    }
]
 */


package com.example.foodJournal.db_extractor;

public class FoodItem {

    private String name ="", ndbno ="";

    public FoodItem(String name_, String ndbno_) {
        name = name_;
        ndbno = ndbno_;
    }

    public String getname() {
        return name;
    }

    public String getndbno() {
        return ndbno;
    }

    public String toString() {
        return name + ", " + ndbno;
    }
}
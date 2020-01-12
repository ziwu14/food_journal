package com.example.foodJournal.db_extractor;

public class RecipeItem {

    private String name = "", url_src="";

    public RecipeItem(String name_, String url_) {
        name = name_;
        url_src = url_;
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url_src;
    }
}

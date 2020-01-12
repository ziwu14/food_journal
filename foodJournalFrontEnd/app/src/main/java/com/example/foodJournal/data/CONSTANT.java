package com.example.foodJournal.data;

public class CONSTANT {
    public static final String dbSocket = "67.159.88.153:8000";

    public static final String authUrl = "http://"+dbSocket+"/food/authentication/";
    public static final String signupUrl = "http://"+dbSocket+"/food/account/";
    public static final String editUrl_incomplete = "http://"+dbSocket+"/food/health_info/?username=";
    public static final String getDangerFoodUrl_incomplete = "http://"+dbSocket+"/food/danger_food/?username=";

    public final static String USDA_api_key = "0HdmUfVCaWe4Bg6vPoaNczZ92tTF3Ao3AiUxbTmW";
    public final static String USDA_searchUrl =  "http://api.nal.usda.gov/ndb/search/?";
    public final static String USDA_reportUrl = " https://api.nal.usda.gov/ndb/reports/?";

    public final static String food2fork_api_key = "afefdd59676a154535120d91d48e151d";
    public final static String food2fork_searchUrl = "https://www.food2fork.com/api/search/?";
    public final static String food2fork_getUrl = "https://www.food2fork.com/api/get/?";
}

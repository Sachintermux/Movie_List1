package com.example.movielist;

import org.json.JSONObject;

public class DataShareActivity {
    public static  JSONObject jsonObject;

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void setJsonObject( JSONObject jsonObject ) {
        DataShareActivity.jsonObject = jsonObject;
    }
}

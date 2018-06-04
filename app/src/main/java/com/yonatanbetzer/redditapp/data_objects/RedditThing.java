package com.yonatanbetzer.redditapp.data_objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditThing {
    private String id;
    private String name;
    private String kind;
    private RedditData data;

    public static ArrayList<RedditThing> fromJsonArray(JSONArray source){
        if(source == null) {
            return new ArrayList<>();
        }
        ArrayList<RedditThing> results = new ArrayList<>();
        for (int i = 0; i < source.length(); i++) {
            RedditThing item = RedditThing.fromJsonObject(source.optJSONObject(i));
            results.add(item);
        }
        return results;
    }

    public static RedditThing fromJsonObject(JSONObject source) {
        if(source == null) {
            return null;
        }
        RedditThing result = new RedditThing();
        result.id = source.optString("id", "");
        result.name = source.optString("name", "");
        result.kind = source.optString("kind", "");
        result.data = RedditData.fromJsonObject(source.optJSONObject("data"));
        return result;
    }
}

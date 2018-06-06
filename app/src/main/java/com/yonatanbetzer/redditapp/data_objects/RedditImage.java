package com.yonatanbetzer.redditapp.data_objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditImage {
    private String url;
    private int width;
    private int height;

    public static ArrayList<RedditImage> fromJsonArray(JSONArray source){
        if(source == null) {
            return new ArrayList<>();
        }
        ArrayList<RedditImage> results = new ArrayList<>();
        for (int i = 0; i < source.length(); i++) {
            RedditImage item = RedditImage.fromJsonObject(source.optJSONObject(i));
            results.add(item);
        }
        return results;
    }

    public static RedditImage fromJsonObject(JSONObject source) {
        if(source == null) {
            return null;
        }
        RedditImage result = new RedditImage();
        result.url = source.optString("url", "");
        result.width = source.optInt("width", 0);
        result.height = source.optInt("height", 0);
        return result;
    }

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

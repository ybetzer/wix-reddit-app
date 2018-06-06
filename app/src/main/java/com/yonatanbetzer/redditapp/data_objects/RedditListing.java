package com.yonatanbetzer.redditapp.data_objects;

import org.json.JSONObject;

import java.util.ArrayList;

public class RedditListing {
    private String before;
    private String after;
    private String modhash;
    private ArrayList<RedditThing> children;

    public static RedditListing fromJsonObject(JSONObject source) {
        if(source == null) {
            return null;
        }

        RedditListing result = new RedditListing();
        result.before = source.optString("before", "");
        result.after = source.optString("after", "");
        result.modhash = source.optString("modhash", "");
        result.children = RedditThing.fromJsonArray(source.optJSONArray("children"));
        return result;
    }

    public ArrayList<RedditThing> getChildren() {
        return children;
    }
}

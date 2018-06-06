package com.yonatanbetzer.redditapp.data_objects;

import org.json.JSONObject;

import java.util.ArrayList;

public class RedditPreview {
    private RedditImage source;
    private ArrayList<RedditImage> resolutions;
    private boolean enabled;

    public static RedditPreview fromJsonObject(JSONObject source) {
        if(source == null) {
            return null;
        }
        RedditPreview result = new RedditPreview();
        result.source = RedditImage.fromJsonObject(source.optJSONObject("source"));
        result.enabled = source.optBoolean("enabled", false);
        result.resolutions = RedditImage.fromJsonArray(source.optJSONArray("resolutions"));
        return result;
    }

    public RedditImage getSource() {
        return source;
    }

    public ArrayList<RedditImage> getResolutions() {
        return resolutions;
    }
}

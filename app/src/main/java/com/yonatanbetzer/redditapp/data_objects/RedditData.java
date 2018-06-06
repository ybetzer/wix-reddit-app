package com.yonatanbetzer.redditapp.data_objects;

import org.json.JSONObject;
import java.util.Date;

public class RedditData {
    private int ups;
    private int downs;
    private long created_utc;
    private Date created_date;
    private String body_html;
    private String permalink;
    private String thumbnail;
    private String title;
    private String url;
    private String header_img;
    private String header_title;
    private String subreddit_name_prefixed;
    private String link_flair_text;
    private RedditPreview preview;

    public static RedditData fromJsonObject(JSONObject source){
        if(source == null) {
            return null;
        }
        RedditData result = new RedditData();
        result.ups = source.optInt("ups", 0);
        result.downs = source.optInt("downs", 0);
        result.created_utc = source.optLong("created_utc", 0);
        result.created_date = new Date(result.created_utc * 1000);
        result.body_html = source.optString("body_html", "");
        result.permalink = source.optString("permalink", "");
        result.thumbnail = source.optString("thumbnail", "");
        result.title = source.optString("title", "");
        result.url = source.optString("url", "");
        result.header_img = source.optString("header_img", "");
        result.header_title = source.optString("header_title", "");
        result.subreddit_name_prefixed = source.optString("subreddit_name_prefixed", "");
        result.link_flair_text = source.optString("link_flair_text", "");
        result.preview = RedditPreview.fromJsonObject(source.optJSONObject("preview"));
        return result;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getBestImageUrl() {
        if(preview != null) {
            if(preview.getSource() != null) {
                return preview.getSource().getUrl();
            }
            if(preview.getResolutions() != null &&
                    preview.getResolutions().size() > 0) {
                return preview.getResolutions().get(preview.getResolutions().size() - 1).getUrl();
            }
        }
        return thumbnail;
    }

    public Date getCreatedDate() {
        return created_date;
    }
}

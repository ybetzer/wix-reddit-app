package com.yonatanbetzer.redditapp.utils;

import android.graphics.Typeface;

import com.yonatanbetzer.redditapp.application.RedditApplication;

public class Constants {
    public static final String ROOT_REDDIT_URL = "https://www.reddit.com/r/politics.json";
    private static final String OPEN_SANS_REGULAR_HEBREW = "OpenSansHebrew-Regular.ttf";

    public static final Typeface openSansRegularHebrew = Typeface.createFromAsset(RedditApplication.getAppContext().getAssets(),Constants.OPEN_SANS_REGULAR_HEBREW);
}

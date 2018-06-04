package com.yonatanbetzer.redditapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class RedditApplication extends Application {
    private static RedditApplication mInstance;
    private static Context mAppContext;
    private static Activity currentActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mAppContext = getApplicationContext();
    }

    public static RedditApplication getInstance(){
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        RedditApplication.currentActivity = currentActivity;
    }
}

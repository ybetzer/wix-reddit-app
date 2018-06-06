package com.yonatanbetzer.redditapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.yonatanbetzer.redditapp.favorites.Favorites;
import com.yonatanbetzer.redditapp.utils.FilterListener;

import java.util.ArrayList;

public class RedditApplication extends Application {
    private static RedditApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppData.getInstance().setAppContext(getApplicationContext());
    }

    public static RedditApplication getInstance(){
        return mInstance;
    }
}

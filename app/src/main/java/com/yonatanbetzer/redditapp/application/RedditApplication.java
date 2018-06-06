package com.yonatanbetzer.redditapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.yonatanbetzer.redditapp.utils.FilterListener;

import java.util.ArrayList;

public class RedditApplication extends Application {
    private static RedditApplication mInstance;
    private static Context mAppContext;
    private AppCompatActivity currentActivity;
    private ArrayList<FilterListener> filterListeners = new ArrayList<>();

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

    public AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(AppCompatActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void addFilterListsner(FilterListener listener) {
        filterListeners.add(listener);
    }

    public void removeFilterListsner(FilterListener listener) {
        filterListeners.remove(listener);
    }

    public void publishFilter(CharSequence value) {
        for (FilterListener listener : filterListeners) {
            if(listener != null) {
                listener.filterResults(value);
            }
        }
    }
}

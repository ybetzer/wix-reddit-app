package com.yonatanbetzer.redditapp.application;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.yonatanbetzer.redditapp.favorites.Favorites;
import com.yonatanbetzer.redditapp.utils.FilterListener;
import java.util.ArrayList;

public class AppData {
    private static AppData instance;
    private Context appContext;
    private AppCompatActivity currentActivity;
    private ArrayList<FilterListener> filterListeners = new ArrayList<>();
    private Favorites favorites;

    public static AppData getInstance(){
        if(instance == null) {
            instance = new AppData();
            instance.favorites = new Favorites();
        }
        return instance;
    }

    public static Context getAppContext() {
        return instance.appContext;
    }

    public void setAppContext(Context context) {
        appContext = context;
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

    public Favorites getFavorites() {
        return favorites;
    }
}

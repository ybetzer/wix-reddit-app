package com.yonatanbetzer.redditapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.yonatanbetzer.redditapp.application.RedditApplication;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utils {
    public static int pixelsFromDP(int dp) {
        DisplayMetrics displaymetrics = RedditApplication.getAppContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, dp, displaymetrics );
    }

    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static void getImageMainColor(Bitmap bitmap, final PaletteListener listener) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(@NonNull Palette palette) {
                if(listener != null) {
                    listener.paletteExtracted(palette);
                }
            }
        });
    }

    public static boolean isToday(Date createdDate){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date midnight = cal.getTime();
        return createdDate.getTime() > midnight.getTime();
    }

    public static DateFormat getDateFormat(Date createdDate) {
        DateFormat dateFormat;
        if(isToday(createdDate)) {
            // yesterday or earlier
            dateFormat = android.text.format.DateFormat.getTimeFormat(RedditApplication.getAppContext());
        } else {
            // since midnight today
            dateFormat = android.text.format.DateFormat.getDateFormat(RedditApplication.getAppContext());
        }
        return dateFormat;
    }
}

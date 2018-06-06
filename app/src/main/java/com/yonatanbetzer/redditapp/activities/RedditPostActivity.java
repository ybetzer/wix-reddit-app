package com.yonatanbetzer.redditapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.application.RedditApplication;

public class RedditPostActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_post_activity);
        webview = findViewById(R.id.webview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RedditApplication.setCurrentActivity(this);
    }
}

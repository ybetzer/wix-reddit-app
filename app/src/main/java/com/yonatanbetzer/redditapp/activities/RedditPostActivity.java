package com.yonatanbetzer.redditapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;
import org.json.JSONException;
import org.json.JSONObject;

public class RedditPostActivity extends AppCompatActivity {
    private WebView webview;
    private RedditThing post;
    private ProgressBar progressBar;
    public static final String EXTRA_REDDIT_THING_JSON = "EXTRA_REDDIT_THING_JSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_post_activity);

        progressBar = findViewById(R.id.progress_bar);
        webview = findViewById(R.id.webview);
        prepareWebview();

        Intent intent = getIntent();
        if(intent != null) {
            String redditThingJsonString = intent.getStringExtra(EXTRA_REDDIT_THING_JSON);
            if(redditThingJsonString != null) {
                try {
                    JSONObject redditThingJsonObject = new JSONObject(redditThingJsonString);
                    post = RedditThing.fromJsonObject(redditThingJsonObject);
                    if(post != null && post.getData() != null && post.getData().getUrl() != null && post.getData().getUrl().length() > 6) {
                        webview.loadUrl(post.getData().getUrl());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void prepareWebview() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebViewClient(new RedditWebviewClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        RedditApplication.getInstance().setCurrentActivity(this);
    }

    private class RedditWebviewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onLoadResource(view, url);
        }
    }
}

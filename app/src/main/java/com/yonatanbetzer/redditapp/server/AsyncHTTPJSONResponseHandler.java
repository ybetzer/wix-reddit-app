package com.yonatanbetzer.redditapp.server;

import org.json.JSONObject;

public interface AsyncHTTPJSONResponseHandler {
    void onSuccess(JSONObject responseBody);

    void onFailure(String error, int errorCode);
}

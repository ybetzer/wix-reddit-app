package com.yonatanbetzer.redditapp.server;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yonatanbetzer.redditapp.application.AppData;
import org.json.JSONObject;

public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(AppData.getAppContext());
    }

    public static VolleySingleton getInstance(){
        if(mInstance == null){
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }

    public void getJSONObjectAsync(final String url,
                                   final AsyncHTTPJSONResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(handler != null) {
                            handler.onSuccess(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(handler != null) {
                            handler.onFailure(error.getLocalizedMessage(), 0);
                        }

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(true);
        getRequestQueue().add(jsonObjectRequest);
    }
}
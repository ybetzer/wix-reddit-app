package com.yonatanbetzer.redditapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class RedditLobbyAdapter extends RecyclerView.Adapter {
    private ArrayList<RedditThing> posts;

    public RedditLobbyAdapter(ArrayList<RedditThing> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

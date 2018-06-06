package com.yonatanbetzer.redditapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.holders.RedditPostHolder;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class RedditLobbyAdapter extends RecyclerView.Adapter {
    private static final int ITEM = 1;
    private int lastPosition = -1;
    private ArrayList<RedditThing> posts;

    public RedditLobbyAdapter(ArrayList<RedditThing> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                viewHolder = createItemViewHolder(parent);
                break;
            default:
                break;
        }
        return viewHolder;
    }


    @NonNull
    private RecyclerView.ViewHolder createItemViewHolder(@NonNull ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reddit_post_holder, parent, false);
        viewHolder = new RedditPostHolder(itemView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                bindItemViewHolder((RedditPostHolder) holder, position);
                setAnimation(holder.itemView, position);
                break;
            default:
                break;
        }
    }


    private void bindItemViewHolder(@NonNull RedditPostHolder holder, int position) {
        if(posts != null && posts.size() > position) {
            RedditThing imageResult = posts.get(position);
            holder.bindTo(imageResult);
        }
    }


    @Override
    public int getItemCount() {
        if(posts == null) {
            return 0;
        }
        return posts.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(RedditApplication.getAppContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

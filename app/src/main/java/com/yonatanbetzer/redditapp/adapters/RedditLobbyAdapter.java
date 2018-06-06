package com.yonatanbetzer.redditapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filterable;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.holders.RedditPostHolder;
import com.yonatanbetzer.redditapp.application.AppData;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;
import java.util.logging.Filter;

public class RedditLobbyAdapter extends RecyclerView.Adapter implements Filterable {
    private static final int ITEM = 1;
    private int lastPosition = -1;
    private ArrayList<RedditThing> posts;
    private ArrayList<RedditThing> postsFiltered;

    public RedditLobbyAdapter(ArrayList<RedditThing> posts) {
        this.posts = posts;
        this.postsFiltered = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createItemViewHolder(parent);
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
        if(postsFiltered != null && postsFiltered.size() > position) {
            RedditThing imageResult = postsFiltered.get(position);
            holder.bindTo(imageResult);
        }
    }

    @Override
    public int getItemCount() {
        if(postsFiltered == null) {
            return 0;
        }
        return postsFiltered.size();
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(AppData.getAppContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.length() < 4) {
                    postsFiltered = posts;
                } else {
                    ArrayList<RedditThing> filteredList = new ArrayList<>();
                    for (RedditThing post : posts) {
                        if (post.getData() != null &&
                                post.getData().getTitle() != null &&
                                post.getData().getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(post);
                        }
                    }
                    postsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = postsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postsFiltered = (ArrayList<RedditThing>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

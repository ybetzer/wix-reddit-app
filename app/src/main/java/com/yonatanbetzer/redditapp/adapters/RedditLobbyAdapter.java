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
import com.yonatanbetzer.redditapp.adapters.holders.RedditThingViewHolder;
import com.yonatanbetzer.redditapp.application.AppData;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class RedditLobbyAdapter extends RecyclerView.Adapter implements Filterable {
    private static final int ITEM = 1;
    private int lastPosition = -1;
    private ArrayList<RedditThing> things;
    private ArrayList<RedditThing> thingsFiltered;

    public RedditLobbyAdapter(ArrayList<RedditThing> things) {
        this.things = things;
        this.thingsFiltered = things;
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
                .inflate(R.layout.reddit_thing_holder, parent, false);
        viewHolder = new RedditThingViewHolder(itemView);
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
                bindItemViewHolder((RedditThingViewHolder) holder, position);
                setAnimation(holder.itemView, position);
                break;
            default:
                break;
        }
    }

    private void bindItemViewHolder(@NonNull RedditThingViewHolder holder, int position) {
        if(thingsFiltered != null && thingsFiltered.size() > position) {
            RedditThing imageResult = thingsFiltered.get(position);
            holder.bindTo(imageResult);
        }
    }

    @Override
    public int getItemCount() {
        if(thingsFiltered == null) {
            return 0;
        }
        return thingsFiltered.size();
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
                    thingsFiltered = things;
                } else {
                    ArrayList<RedditThing> filteredList = new ArrayList<>();
                    for (RedditThing thing : things) {
                        if (thing.getData() != null &&
                                thing.getData().getTitle() != null &&
                                thing.getData().getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(thing);
                        }
                    }
                    thingsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = thingsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                thingsFiltered = (ArrayList<RedditThing>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

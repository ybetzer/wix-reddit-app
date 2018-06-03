package com.yonatanbetzer.redditapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.RedditLobbyAdapter;

public class RedditLobby extends android.support.v4.app.Fragment {
    public static final String TAB_DATA_SOURCE = "tab_data_source";

    private RecyclerView postListRecyclerView;
    private RedditLobbyAdapter postListAdapter;

    public enum TabDataSource {
        reddit,
        favorites
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.reddit_lobby_fragment, container, false);


        return rootView;
    }

}

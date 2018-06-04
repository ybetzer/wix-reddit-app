package com.yonatanbetzer.redditapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.RedditLobbyAdapter;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class RedditLobby extends android.support.v4.app.Fragment {
    public static final String TAB_DATA_SOURCE = "tab_data_source";

    private RecyclerView postsRecyclerView;
    private RedditLobbyAdapter postListAdapter;
    private RecyclerView.LayoutManager linearLayoutManager;
    private ArrayList<RedditThing> posts = new ArrayList<>();

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
        postsRecyclerView = rootView.findViewById(R.id.posts_recycler_view);
        postListAdapter = new RedditLobbyAdapter(posts);
        postsRecyclerView.setAdapter(postListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        return rootView;
    }

}

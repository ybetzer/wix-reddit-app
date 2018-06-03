package com.yonatanbetzer.redditapp.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yonatanbetzer.redditapp.fragments.RedditLobby;

public class TabsViewPagerAdapter extends FragmentPagerAdapter {
    public TabsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new RedditLobby();
        Bundle args = new Bundle();

        args.putInt(RedditLobby.TAB_DATA_SOURCE, (position == 0)? RedditLobby.TabDataSource.reddit.ordinal() : RedditLobby.TabDataSource.favorites.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Reddit";
            case 1: return "Favorites";
            default:
                break;
        }
        return "Error";
    }
}

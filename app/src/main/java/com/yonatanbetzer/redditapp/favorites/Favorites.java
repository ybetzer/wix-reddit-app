package com.yonatanbetzer.redditapp.favorites;

import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class Favorites {
    private ArrayList<RedditThing> favorites;

    public Favorites() {
        favorites = new ArrayList<>();
    }

    public void add(RedditThing post){
        favorites.add(post);
    }

    public void remove(RedditThing post) {
        favorites.remove(post);
    }

    public boolean isInFavorites(RedditThing post) {
        return favorites.contains(post);
    }

    public ArrayList<RedditThing> getFavorites() {
        return favorites;
    }
}

package com.yonatanbetzer.redditapp.favorites;

import com.yonatanbetzer.redditapp.data_objects.RedditThing;

import java.util.ArrayList;

public class Favorites {
    private static ArrayList<RedditThing> favorites = new ArrayList<>();

    public static void add(RedditThing post){
        favorites.add(post);
    }

    public static void remove(RedditThing post) {
        favorites.remove(post);
    }

    public static boolean isInFavorites(RedditThing post) {
        return favorites.contains(post);
    }

    public static ArrayList<RedditThing> getFavorites() {
        return favorites;
    }
}

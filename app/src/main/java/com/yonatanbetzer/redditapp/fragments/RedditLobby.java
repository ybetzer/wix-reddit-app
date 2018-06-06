package com.yonatanbetzer.redditapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.RedditLobbyAdapter;
import com.yonatanbetzer.redditapp.application.RedditApplication;
import com.yonatanbetzer.redditapp.data_objects.RedditListing;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;
import com.yonatanbetzer.redditapp.favorites.Favorites;
import com.yonatanbetzer.redditapp.server.AsyncHTTPJSONResponseHandler;
import com.yonatanbetzer.redditapp.server.VolleySingleton;
import com.yonatanbetzer.redditapp.utils.Constants;
import com.yonatanbetzer.redditapp.utils.FilterListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class RedditLobby extends android.support.v4.app.Fragment implements FilterListener {
    public static final String TAB_DATA_SOURCE = "tab_data_source";

    private RecyclerView postsRecyclerView;
    private RedditLobbyAdapter postListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeToRefreshLayout;
    private ProgressBar progressBar;
    private View loadingMore;

    private RedditListing lastRedditListing;
    private ArrayList<RedditThing> posts = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isRefresh = false;

    private TabDataSource dataSource;

    public enum TabDataSource {
        reddit,
        favorites
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            String tabDataSourceName = args.getString(TAB_DATA_SOURCE);
            dataSource = TabDataSource.valueOf(tabDataSourceName);
        }
        RedditApplication.getInstance().addFilterListsner(this);
    }

    @Override
    public void onDestroy() {
        RedditApplication.getInstance().removeFilterListsner(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.reddit_lobby_fragment, container, false);
        postsRecyclerView = rootView.findViewById(R.id.posts_recycler_view);
        postListAdapter = new RedditLobbyAdapter(posts);
        postsRecyclerView.setAdapter(postListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        postsRecyclerView.setLayoutManager(linearLayoutManager);
        progressBar = rootView.findViewById(R.id.progress_bar);

        swipeToRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                posts.clear();
                lastRedditListing = null;
                fetchDataPage();
            }
        });
        loadingMore = rootView.findViewById(R.id.loading_more);
        TextView loadingMoreTextView = rootView.findViewById(R.id.loading_more_text);
        loadingMoreTextView.setTypeface(Constants.openSansRegularHebrew);

        fetchDataPage();

        if(dataSource == TabDataSource.reddit) {
            postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int dataItemCount = linearLayoutManager.getItemCount();
                    int viewsPerPage = linearLayoutManager.getChildCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((viewsPerPage + firstVisibleItemPosition) >= dataItemCount - 10
                                && firstVisibleItemPosition >= 0
                                && dataItemCount >= 10) {
                            showLoadingMore();
                            fetchDataPage();
                        }
                    }
                }
            });
        }

        rootView.requestFocus();
        return rootView;
    }

    private void showLoadingMore() {
        if(loadingMore != null) {
            loadingMore.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingMore() {
        if(loadingMore != null) {
            loadingMore.setVisibility(View.GONE);
        }
    }

    private void fetchDataPage() {
        if(isRefresh) {
            swipeToRefreshLayout.setRefreshing(true);
        }
        switch (dataSource) {
            case reddit:
                fetchDataPageFromReddit();
            break;
            case favorites:
                fetchDataPageFromFavorites();
            break;
        }
    }

    private void fetchDataPageFromFavorites() {
        posts = Favorites.getFavorites();
        swipeToRefreshLayout.setRefreshing(false);
        postListAdapter.notifyDataSetChanged();
        hideLoadingMore();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private void fetchDataPageFromReddit() {
        String url = Constants.ROOT_REDDIT_URL;
        if(posts.size() == 0 && !isRefresh) {
            progressBar.setVisibility(View.VISIBLE);
        }

        if(lastRedditListing != null && lastRedditListing.getAfter() != null) {
            String separator = (url.contains("?")? "&":"?");
            url += separator + "after=" + lastRedditListing.getAfter();
        }
        isLoading = true;

        VolleySingleton.getInstance().getJSONObjectAsync(url, new AsyncHTTPJSONResponseHandler() {
            @Override
            public void onSuccess(JSONObject responseBody) {
                isRefresh = false;
                swipeToRefreshLayout.setRefreshing(false);
                RedditListing listing = RedditListing.fromJsonObject(responseBody);
                lastRedditListing = listing;
                posts.addAll(listing.getChildren());
                postListAdapter.notifyDataSetChanged();
                hideLoadingMore();
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(String error, int errorCode) {
                isRefresh = false;
                swipeToRefreshLayout.setRefreshing(false);
                hideLoadingMore();
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }


    @Override
    public void filterResults(CharSequence value) {
        postListAdapter.getFilter().filter(value);
    }
}

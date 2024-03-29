package com.yonatanbetzer.redditapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.RedditLobbyAdapter;
import com.yonatanbetzer.redditapp.application.AppData;
import com.yonatanbetzer.redditapp.data_objects.RedditListing;
import com.yonatanbetzer.redditapp.data_objects.RedditThing;
import com.yonatanbetzer.redditapp.server.AsyncHTTPJSONResponseHandler;
import com.yonatanbetzer.redditapp.server.VolleySingleton;
import com.yonatanbetzer.redditapp.utils.Constants;
import com.yonatanbetzer.redditapp.utils.FilterListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class RedditLobby extends android.support.v4.app.Fragment implements FilterListener {
    public static final String TAB_DATA_SOURCE = "tab_data_source";

    private RecyclerView redditThingsRecyclerView;
    private RedditLobbyAdapter redditThingsListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeToRefreshLayout;
    private ProgressBar progressBar;
    private View loadingMore;
    private View scrollTopIcon;
    private ViewGroup noResultsContainer;
    private ImageView noResultsImage;
    private TextView noResultsText;

    private RedditListing lastRedditListing;
    private ArrayList<RedditThing> things = new ArrayList<>();

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
        AppData.getInstance().addFilterListsner(this);
    }

    @Override
    public void onDestroy() {
        AppData.getInstance().removeFilterListsner(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.reddit_lobby_fragment, container, false);
        redditThingsRecyclerView = rootView.findViewById(R.id.posts_recycler_view);
        redditThingsListAdapter = new RedditLobbyAdapter(things);
        redditThingsRecyclerView.setAdapter(redditThingsListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        redditThingsRecyclerView.setLayoutManager(linearLayoutManager);
        progressBar = rootView.findViewById(R.id.progress_bar);

        scrollTopIcon = rootView.findViewById(R.id.scroll_top_icon);
        scrollTopIcon.setVisibility(View.GONE);
        scrollTopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redditThingsRecyclerView.smoothScrollToPosition(0);
            }
        });
        swipeToRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                things.clear();
                lastRedditListing = null;
                fetchDataPage();
            }
        });
        loadingMore = rootView.findViewById(R.id.loading_more);
        TextView loadingMoreTextView = rootView.findViewById(R.id.loading_more_text);
        loadingMoreTextView.setTypeface(Constants.openSansRegularHebrew);

        if(dataSource == TabDataSource.reddit) {
            redditThingsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            scrollTopIcon.setAlpha(1.0f);
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            scrollTopIcon.setAlpha(0.5f);
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            scrollTopIcon.setAlpha(0.75f);
                            break;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int dataItemCount = linearLayoutManager.getItemCount();
                    int viewsPerPage = linearLayoutManager.getChildCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if(firstVisibleItemPosition > 5) {
                        scrollTopIcon.setVisibility(View.VISIBLE);
                    } else {
                        scrollTopIcon.setVisibility(View.GONE);
                    }
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

        if(dataSource == TabDataSource.favorites) {
            noResultsContainer = rootView.findViewById(R.id.no_results_container);
            noResultsImage = rootView.findViewById(R.id.no_results_image);
            noResultsText = rootView.findViewById(R.id.no_results_text);
            noResultsText.setTypeface(Constants.openSansRegularHebrew);
            Glide.with(this).load(R.drawable.vincent_vega).into(noResultsImage);
        }

        fetchDataPage();

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
        redditThingsRecyclerView.requestFocus();
    }

    private void fetchDataPageFromFavorites() {
        ArrayList<RedditThing> favorites = AppData.getInstance().getFavorites().getFavorites();
        if(favorites.size() == 0) {
            noResultsContainer.setVisibility(View.VISIBLE);
            redditThingsRecyclerView.setVisibility(View.GONE);
        } else {
            noResultsContainer.setVisibility(View.GONE);
            redditThingsRecyclerView.setVisibility(View.VISIBLE);
        }
        things.clear();
        things.addAll(favorites);
        swipeToRefreshLayout.setRefreshing(false);
        redditThingsListAdapter.notifyDataSetChanged();
        hideLoadingMore();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private void fetchDataPageFromReddit() {
        String url = Constants.ROOT_REDDIT_URL;
        if(things.size() == 0 && !isRefresh) {
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
                things.addAll(listing.getChildren());
                redditThingsListAdapter.notifyDataSetChanged();
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
        redditThingsListAdapter.getFilter().filter(value);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataSource == TabDataSource.favorites) {
            fetchDataPageFromFavorites();
        }
    }
}

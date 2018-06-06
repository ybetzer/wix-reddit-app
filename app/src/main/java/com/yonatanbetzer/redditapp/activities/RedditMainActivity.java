package com.yonatanbetzer.redditapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.TabsViewPagerAdapter;
import com.yonatanbetzer.redditapp.application.AppData;

public class RedditMainActivity extends AppCompatActivity {

    ViewPager tabsViewPager;
    TabsViewPagerAdapter tabsAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("r/politics"));
        tabLayout.addTab(tabLayout.newTab().setText("favorites"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabsViewPager = findViewById(R.id.tabs_view_pager);
        tabsAdapter = new TabsViewPagerAdapter(getSupportFragmentManager());
        tabsViewPager.setAdapter(tabsAdapter);

        tabsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppData.getInstance().setCurrentActivity(this);
        removeFocusFromSearchView();
        if(searchView != null) {
            searchView.setQuery("", false);
        }
        AppData.getInstance().publishFilter("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppData.getInstance().publishFilter(query);
                removeFocusFromSearchView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                AppData.getInstance().publishFilter(query);
                return false;
            }
        });
        return true;
    }


    private void removeFocusFromSearchView() {
        if(searchView != null && searchView.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

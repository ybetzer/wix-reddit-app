package com.yonatanbetzer.redditapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yonatanbetzer.redditapp.R;
import com.yonatanbetzer.redditapp.adapters.TabsViewPagerAdapter;

public class RedditMainActivity extends AppCompatActivity {

    ViewPager tabsViewPager;
    TabsViewPagerAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Reddit"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));
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
}

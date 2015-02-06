package com.ritwik.android.madfbla201415;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

// joshua zhou
public class HelpActivity extends ActionBarActivity {

    // add fragments for features
    private Fragment[] features = new Fragment[] {

            //Home Basics
            HelpScreenSlidePageFragment.instance(
                    "How to use this app",
                    "At home, browse events with the scrolling image banner or events list. Click on " +
                            "an event to see details. You can see events through the calendar, or see " +
                            "all events as a list. Other features include Search, Voice Search, RSVP, " +
                            "Sync events with Calendar, and Notifications. Chronology is the best way " +
                            "to keep track of Homestead High School Events!",
                    R.drawable.sc_home
            ),

            HelpScreenSlidePageFragment.instance(
                    "RSVP and Sync with Calendar",
                    "RSVP to events by clicking the RSVP Button. Your RSVP'd events will be highlighted " +
                            "green, you can cancel your RSVP at any time. You can sync the events in " +
                            "Chronology with the Calendar app, and therefore Google Calendar. Simply press " +
                            "the button, and you will be redirected to the Calendar App with all information " +
                            "already filled out. Make any required changes.",
                    R.drawable.sc_rsvp_sync
            ),

            HelpScreenSlidePageFragment.instance(
                    "Notifications",
                    "Homestead High School can send out notifications for events. CLick on a notification " +
                            "to see more details. You can see all your notifications at any time.",
                    R.drawable.sc_notifications

            ),

            HelpScreenSlidePageFragment.instance(
                    "Event Filters",
                    "Filter events in All Events",
                    R.drawable.sc_filters

            ),

            //Search
            HelpScreenSlidePageFragment.instance(
                    "Search",
                    "Search events using the Search Widget in the Action Bar. Search for the title" +
                            "location, or details of an event. Voice Search is also available.",
                    R.drawable.sc_home
            )


    };

    private ViewPager mPager;
    private static final String LOG_TAG = "Help";

    private GestureDetectorCompat mLeftDetector;
    private View.OnTouchListener mListener;
    private SwipeListener mFlinglistener;
    private Toolbar toolbar;
    private SearchView mSearch;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        toolbar.inflateMenu(R.menu.menu_main);
        initDrawer();

        mFlinglistener = new SwipeListener();
        mLeftDetector = new GestureDetectorCompat(this, mFlinglistener);
        mListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touch = mLeftDetector.onTouchEvent(event);
                if (mFlinglistener.hasSwiped()) {
                    finish();
                    HelpActivity.this.overridePendingTransition(
                            R.anim.neg_left_right, R.anim.left_to_right);
                }
                return touch;
            }
        };

        mPager = (ViewPager) findViewById(R.id.pager);
        List<Fragment> fragments = getFragments();
        mPager.setAdapter(
                new ScreenSlidePagerAdapter(
                        getSupportFragmentManager(),
                        fragments
                )
        );

    }

    private List<Fragment> getFragments() {
        Bundle b1 = new Bundle();
        b1.putString("name", "abc");

        Bundle b2 = new Bundle();
        b2.putString("name", "d");

        Bundle b3 = new Bundle();
        b3.putString("name", "aq");

        Bundle b4 = new Bundle();
        b4.putString("name", "aq3");

        Bundle b5 = new Bundle();
        b5.putString("name", "aq4");

        Fragment f1 = new HelpScreenSlidePageFragment();
        f1.setArguments(b1);

        Fragment f2 = new HelpScreenSlidePageFragment();
        f2.setArguments(b2);

        Fragment f3 = new HelpScreenSlidePageFragment();
        f3.setArguments(b3);

        Fragment f4 = new HelpScreenSlidePageFragment();
        f4.setArguments(b4);

        Fragment f5 = new HelpScreenSlidePageFragment();
        f5.setArguments(b5);



        return Arrays.asList(new Fragment[] { f1, f2, f3, f4, f5 });
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        DrawerAdapter mDrawerAdapter = new DrawerAdapter(this, DataHolder.getDrawerArray());
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
                this, mDrawerLayout, mDrawerList));

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //set all items unchecked
                for (int i = 0; i < DataHolder.getDrawerArray().length; i++) {
                    mDrawerList.setItemChecked(i, false);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerList.setBackgroundResource(R.color.drawer_background);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //android library API 11+ standard search
        SearchManager managerSearch = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.chronology_search_bar).getActionView();
        mSearch.setSearchableInfo(managerSearch.getSearchableInfo(getComponentName()));
        mSearch.setIconifiedByDefault(true);

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.chronology_search_bar) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }

    // Slide pager
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {

            return features[i];
        }

        @Override
        public int getCount() {
            return features.length;
        }

        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}

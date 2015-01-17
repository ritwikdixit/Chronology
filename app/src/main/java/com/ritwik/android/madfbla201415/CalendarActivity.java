package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by Ritwik on 1/7/15.
 */
public class CalendarActivity extends ActionBarActivity {

    private ListView mEventsList;
    private EventListItemAdapter adapter;

    private ArrayList<EventItem> events;
    private ArrayList<EventItem> filteredEvents;

    private CalendarView mCalendar;

    private Activity mContext = this;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "Calendar";

    private Toolbar toolbar;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        toolbar.inflateMenu(R.menu.menu_main);

        //init the drawer
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


        events = new ArrayList<>(HomepageFragment.getEvents());
        filteredEvents = new ArrayList<>();

        mEventsList =  (ListView) findViewById(R.id.events_per_day_listView);
        adapter = new EventListItemAdapter(this, filteredEvents);
        mEventsList.setAdapter(adapter);

        mCalendar = (CalendarView) findViewById(R.id.calendar_chronology);
        initializeCalendar();
        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month++;
                String monthStr = "", dayStr = "";

                if (month < 10) {
                    monthStr = "0"+month;
                } else {
                    monthStr = ""+month;
                }

                if (dayOfMonth < 10) {
                    dayStr = "0"+dayOfMonth;
                } else {
                    dayStr = ""+dayOfMonth;
                }

                String date = year + "-" + monthStr + "-" + dayStr;
                filterEvents(date, events);
                mEventsList.setAdapter(adapter);
            }
        });

        mEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Putting data for detail activity
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, position + 1);

                detailIntent.putExtra(HomepageFragment.TITLE_KEY,
                        filteredEvents.get(position).getmTitle());
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY, filteredEvents.get(position)
                        .formatDate(filteredEvents.get(position).getmStartDate()));
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY, filteredEvents.get(position)
                        .formatDate(filteredEvents.get(position).getmEndDate()));
                detailIntent.putExtra(HomepageFragment.START_TIME_KEY,
                        filteredEvents.get(position).getmStartTime());
                detailIntent.putExtra(HomepageFragment.END_TIME_KEY,
                        filteredEvents.get(position).getmEndTime());
                detailIntent.putExtra(HomepageFragment.LOCATION_KEY,
                        filteredEvents.get(position).getmLocation());
                detailIntent.putExtra(HomepageFragment.DETAILS_KEY,
                        filteredEvents.get(position).getmDetails());
                detailIntent.putExtra(HomepageFragment.URL_KEY,
                        filteredEvents.get(position).getmUrl());
                detailIntent.putExtra(HomepageFragment.CONTACT_INFO_KEY,
                        filteredEvents.get(position).getmContactInfo());

                startActivity(detailIntent);
                CalendarActivity.this.overridePendingTransition(
                        R.anim.right_to_left, R.anim.neg_right_left);

            }
        });

    }

    private void initializeCalendar() {
        mCalendar.setShowWeekNumber(false);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.chronology_search_bar) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void filterEvents(String todayDate, ArrayList<EventItem> allEvents) {

        filteredEvents.clear();

        for (EventItem thisEvent : allEvents) {
            if (thisEvent.getmStartDate().compareTo(todayDate) < 1
                    && thisEvent.getmEndDate().compareTo(todayDate) > -1) {
                filteredEvents.add(thisEvent);
            }
        }

    }



}

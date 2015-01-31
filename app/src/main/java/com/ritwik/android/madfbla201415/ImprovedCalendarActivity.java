package com.ritwik.android.madfbla201415;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.roomorama.caldroid.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ImprovedCalendarActivity extends ActionBarActivity {
    private ListView mEventsList;
    private EventListItemAdapter adapter;

    private ArrayList<EventItem> events;
    private ArrayList<EventItem> filteredEvents;

    private TextView mDataText;
    private CaldroidFragment caldroidFragment;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static final String TAG = "Calendar";

    private Toolbar toolbar;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);

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
        mDataText = (TextView) findViewById(R.id.calendar_data_text);
        mDataText.setText("Events for Today");


        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        filterEvents(date, events);
        setText(date);
        mEventsList.setAdapter(adapter);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        caldroidFragment.setBackgroundResourceForDate(R.color.calendar_blue, new Date());

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar_chronology, caldroidFragment);
        t.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
    public void filterEvents(String todayDate, ArrayList<EventItem> allEvents) {

        filteredEvents.clear();

        for (EventItem thisEvent : allEvents) {
            if (thisEvent.getmStartDate().compareTo(todayDate) < 1
                    && thisEvent.getmEndDate().compareTo(todayDate) > -1) {
                DateFormat d = new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH);
                try {
                    Date x = d.parse(todayDate);
                    caldroidFragment.setBackgroundResourceForDate(R.color.calendar_blue, x);

                } catch (ParseException e) {}
                filteredEvents.add(thisEvent);

            }
        }

    }
    private void setText(String date) {

        if (filteredEvents.size() == 0) {
            mDataText.setText("No Events on " + EventItem.formatDate(date));
        } else if (filteredEvents.size() == 1) {
            mDataText.setText(1 + " Event on " + EventItem.formatDate(date));
        } else {
            mDataText.setText(filteredEvents.size() + " Events on " + EventItem.formatDate(date));
        }
    }
}

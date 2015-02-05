package com.ritwik.android.madfbla201415;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import hirondelle.date4j.DateTime;


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
    public static final String TAG = "Calendar Improved";

    private Toolbar toolbar;
    private SearchView mSearch;




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
        initDrawer();

        events = new ArrayList<>(HomepageFragment.getEvents());
        filteredEvents = new ArrayList<>();

        mEventsList =  (ListView) findViewById(R.id.events_per_day_listView);
        adapter = new EventListItemAdapter(this, filteredEvents);
        mEventsList.setAdapter(adapter);
        mDataText = (TextView) findViewById(R.id.calendar_data_text);
        mDataText.setText("Events for Today");

        setText(Calendar.getInstance().getTime());
        mEventsList.setAdapter(adapter);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        caldroidFragment.setArguments(args);
        for(EventItem ei : events){
            ArrayList<Date> ald = between(ei.dateStart(), ei.dateEnd());
            for(Date d : ald) {
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, d);
                caldroidFragment.setTextColorForDate(R.color.almost_white, d);
            }
        }
        final AtomicReference<Date> lastDate = new AtomicReference<Date>(Calendar.getInstance().getTime());
        final CaldroidListener listener = new CaldroidListener() {
            public void onSelectDate(Date date, View view) {
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, lastDate.get());
                caldroidFragment.setTextColorForDate(R.color.caldroid_black, lastDate.get());

                lastDate.set(date);

                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_light, date);
                caldroidFragment.setTextColorForDate(R.color.almost_white, date);

                caldroidFragment.refreshView();
                filterEvents(date, events);
                setText(date);
                mEventsList.setAdapter(adapter);
            }
        };
        caldroidFragment.setCaldroidListener(listener);
        caldroidFragment.refreshView();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_chronology, caldroidFragment);
        t.commit();
    }

    private void filterEvents(Date date, ArrayList<EventItem> allEvents) {
        filteredEvents.clear();

        for (EventItem thisEvent : allEvents) {
            if (thisEvent.dateStart().compareTo(date) < 1
                    && thisEvent.dateEnd().compareTo(date) > -1) {
                filteredEvents.add(thisEvent);
            }
        }
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

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
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
    private void setText(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(date);
        if (filteredEvents.size() == 0) {
            mDataText.setText("No Events on " + EventItem.formatDate(todayDate));

        } else if (filteredEvents.size() == 1) {
            mDataText.setText(1 + " Event on " + EventItem.formatDate(todayDate));
        } else {
            mDataText.setText(filteredEvents.size() + " Events on " + EventItem.formatDate(todayDate));
        }
    }
    private ArrayList<Date> between(Date start, Date end){
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);

        while (calendar.getTime().before(end)){
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}
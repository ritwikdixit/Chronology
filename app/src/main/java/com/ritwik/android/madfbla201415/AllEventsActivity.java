package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ritwik on 12/31/14.
 */

public class AllEventsActivity extends ActionBarActivity  {

    private ListView mAllEventsView;
    private EventListItemAdapter adapter;
    private ArrayList<EventItem> events;
    private ArrayList<EventItem> filteredEvents;

    private Activity mContext = this;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Spinner filterChooser;

    private Toolbar toolbar;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevents);

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

        mAllEventsView =  (ListView) findViewById(R.id.all_events_listView);
        events = new ArrayList<>();
        filteredEvents = new ArrayList<>();

        Query eventsByDate = DataHolder.getRef().child("calendar").orderByChild("start_date");
        eventsByDate.addChildEventListener(new ChildEventListener() {

            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                events.add(new EventItem(
                        newEvent.get("id").toString(),
                        newEvent.get("start_date").toString(),
                        newEvent.get("end_date").toString(),
                        newEvent.get("start_time").toString(),
                        newEvent.get("end_time").toString(),
                        newEvent.get("title").toString(),
                        newEvent.get("location").toString(),
                        newEvent.get("details").toString(),
                        newEvent.get("url").toString(),
                        newEvent.get("contact_info").toString(),
                        getApplicationContext()
                ));
                filteredEvents = new ArrayList<>(events);
                mAllEventsView.setAdapter(adapter);

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String removedID = dataSnapshot.child("id").getValue().toString();
                Iterator<EventItem> x  = events.iterator();
                while(x.hasNext()) {
                    EventItem t = x.next();
                    if (t.getId().equals(removedID))
                        x.remove();
                }
                mAllEventsView.setAdapter(adapter);
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Loading Event Item Error", firebaseError.getMessage());
            }
        });

        filterChooser = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.filters, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterChooser.setAdapter(spinnerAdapter);
        filterChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterForCode(position);
                adapter = new EventListItemAdapter(mContext, filteredEvents);
                mAllEventsView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter = new EventListItemAdapter(this, events);
        mAllEventsView.setAdapter(adapter);

        mAllEventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Putting data for detail activity
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, position + 1);

                detailIntent.putExtra(HomepageFragment.TITLE_KEY,
                        events.get(position).getmTitle());
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY,
                        events.get(position).getmStartDate());
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY,
                        events.get(position).getmEndDate());
                detailIntent.putExtra(HomepageFragment.START_TIME_KEY,
                        events.get(position).getmStartTime());
                detailIntent.putExtra(HomepageFragment.END_TIME_KEY,
                        events.get(position).getmEndTime());
                detailIntent.putExtra(HomepageFragment.LOCATION_KEY,
                        events.get(position).getmLocation());
                detailIntent.putExtra(HomepageFragment.DETAILS_KEY,
                        events.get(position).getmDetails());
                detailIntent.putExtra(HomepageFragment.URL_KEY,
                        events.get(position).getmUrl());
                detailIntent.putExtra(HomepageFragment.CONTACT_INFO_KEY,
                        events.get(position).getmContactInfo());

                startActivity(detailIntent);
                AllEventsActivity.this.overridePendingTransition(
                        R.anim.right_to_left, R.anim.neg_right_left);

            }
        });

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

    public void filterForCode(int code) {

        filteredEvents.clear();
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        Log.v(CalendarActivity.TAG, todayDate);

        //0 none , 1 from today, 2 this week, 3 this month
        if (code == 0) {
            filteredEvents = new ArrayList<>(events);

        } else if (code == 1) {
            for (EventItem thisEvent : events) {
                if (thisEvent.getmStartDate().compareTo(todayDate) > -1) {
                    filteredEvents.add(thisEvent);
                }
            }
        } else if (code == 2) {
            for (EventItem thisEvent : events) {
                if (isInThisWeek(thisEvent.getmStartDate())) {
                    filteredEvents.add(thisEvent);
                }
            }

        } else if (code == 3) {
            for (EventItem thisEvent : events) {
                if (isInThisMonth(thisEvent.getmStartDate())) {
                    filteredEvents.add(thisEvent);
                }
            }
        }

    }

    private boolean isInThisWeek(String date) {

        String[] values = date.split("-");
        Calendar c = Calendar.getInstance();

        c.set(Integer.valueOf(values[0]),
                Integer.valueOf(values[1]) - 1,
                Integer.valueOf(values[2]));

        Calendar today = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int week = c.get(Calendar.WEEK_OF_YEAR);

        int year2 = today.get(Calendar.YEAR);
        int week2 = today.get(Calendar.WEEK_OF_YEAR);

        if (year == year2) {
            if (week == week2)
                return true;

        } else if (Math.abs(year - year2) == 1) {
            //this is for example Dec 31 and Jan 2
            if (Math.abs(week2 - week) == 51 && (week == 0 || week2 == 0))
                return true;
        }

        return false;
    }

    private boolean isInThisMonth(String date) {

        String[] values = date.split("-");
        Calendar c = Calendar.getInstance();

        c.set(Integer.valueOf(values[0]),
                Integer.valueOf(values[1]) - 1,
                Integer.valueOf(values[2]));

        Calendar today = Calendar.getInstance();

        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);

        int year2 = today.get(today.YEAR);
        int month2 = today.get(today.MONTH);

        if (year == year2) {
            if (month == month2)
                return true;

        } else if (Math.abs(year - year2) == 1) {
            //this is for example Dec 31 and Jan 30
            if (Math.abs(month2 - month) == 11 && (month == 0 || month2 == 0))
                return true;
        }

        return false;
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


}

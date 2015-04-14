package com.ritwik.madfbla201415;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ritwik.android.madfbla201415.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ImprovedCalendarActivity extends ActionBarActivity {
    private ListView mEventsList;
    private EventListItemAdapter adapter;
    private Context mContext = this;

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
        final Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        caldroidFragment.setArguments(args);



        for(EventItem ei : HomepageFragment.getEvents()){
            ArrayList<Date> ald = between(ei.dateStart(false), ei.dateEnd(false));
            for(Date d : ald) {
                filterEvents(d);
                if (!filteredEvents.isEmpty()) {
                    caldroidFragment.setBackgroundResourceForDate(R.color.calendar_orange, d);
                    caldroidFragment.setTextColorForDate(R.color.almost_white, d);
                }
            }
        }

        final CaldroidListener listener = new CaldroidListener() {
            public void onSelectDate(Date date, View view) {
                filterEvents(date);
                setText(date);
                mEventsList.setAdapter(adapter);

                caldroidFragment.refreshView();
            }
        };
        caldroidFragment.setCaldroidListener(listener);


        caldroidFragment.refreshView();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_chronology, caldroidFragment);
        filterEvents(new Date());
        t.commit();

        addEventClicker();
    }

    private void addEventClicker() {
        mEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Putting data for detail activity
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT,
                        filteredEvents.get(position).getNumber());
                detailIntent.putExtra(HomepageFragment.TITLE_KEY,
                        filteredEvents.get(position).getmTitle());
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY,
                        events.get(position).getmStartDate());
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY,
                        events.get(position).getmEndDate());
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
                detailIntent.putExtra(HomepageFragment.CATEGORY_KEY,
                        filteredEvents.get(position).getCategory());
                detailIntent.putExtra(HomepageFragment.RSVP_KEY,
                        filteredEvents.get(position).isAttending());

                startActivity(detailIntent);
                ImprovedCalendarActivity.this.overridePendingTransition(
                        R.anim.right_to_left, R.anim.neg_right_left);

            }
        });

    }

    private void filterEvents(Date date) {
        filteredEvents.clear();

        for (EventItem thisEvent : HomepageFragment.getEvents()) {
            if (thisEvent.dateStart(true).compareTo(date) < 1
                    && thisEvent.dateEnd(true).compareTo(date) > -1) {
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
    private ArrayList<Date> between(Date start, Date end) {
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
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    public boolean equals(Date a, Date b){
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
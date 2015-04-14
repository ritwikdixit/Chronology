package com.ritwik.madfbla201415;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ritwik.android.madfbla201415.R;

import java.util.ArrayList;
import java.util.Iterator;


//Do not start this activity unless there is an intent with
//a search query. That is the sole purpose of this class.
public class SearchActivity extends ActionBarActivity {

    private String mQuery;
    private Toolbar toolbar;

    private ListView mEventsView;
    private EventListItemAdapter adapter;
    private ArrayList<EventItem> events;
    private int TOTAL_TO_SEARCH = 14;

    //this is what you update in the search
    private ArrayList<EventItem> mData;

    public static final String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //standard action bar stuff
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init the data holders
        mData = new ArrayList<>();
        events = new ArrayList<>(HomepageFragment.getEvents());

        mEventsView = (ListView) findViewById(R.id.search_list_view);

        mEventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, mData.get(position).getNumber());

                detailIntent.putExtra(HomepageFragment.TITLE_KEY,
                        mData.get(position).getmTitle());
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY,
                        events.get(position).getmStartDate());
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY,
                        events.get(position).getmEndDate());
                detailIntent.putExtra(HomepageFragment.START_TIME_KEY,
                        mData.get(position).getmStartTime());
                detailIntent.putExtra(HomepageFragment.END_TIME_KEY,
                        mData.get(position).getmEndTime());
                detailIntent.putExtra(HomepageFragment.LOCATION_KEY,
                        mData.get(position).getmLocation());
                detailIntent.putExtra(HomepageFragment.DETAILS_KEY,
                        mData.get(position).getmDetails());
                detailIntent.putExtra(HomepageFragment.URL_KEY,
                        mData.get(position).getmUrl());
                detailIntent.putExtra(HomepageFragment.CONTACT_INFO_KEY,
                        mData.get(position).getmContactInfo());
                detailIntent.putExtra(HomepageFragment.CATEGORY_KEY,
                        mData.get(position).getCategory());
                detailIntent.putExtra(HomepageFragment.RSVP_KEY,
                        mData.get(position).isAttending());

                startActivity(detailIntent);
                SearchActivity.this.overridePendingTransition(
                        R.anim.right_to_left, R.anim.neg_right_left);
            }
        });

        //handle the incoming android standard search intent
        Intent intent = getIntent();
        if ((intent.getAction()).equals(Intent.ACTION_SEARCH)) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.v(TAG, "success");
            getSupportActionBar().setTitle("Search: " + mQuery);
            doTheSearch(mQuery);
        }
    }

    public void doTheSearch(String query) {

            query = query.toLowerCase();
            int found = 0;
            Iterator<EventItem> iterator = events.iterator();
            while (found < TOTAL_TO_SEARCH && iterator.hasNext()) {
                EventItem event = iterator.next();
                if (event.getmLocation().toLowerCase().contains(query) ||
                        event.getmTitle().toLowerCase().contains(query) ||
                        event.getmDetails().toLowerCase().contains(query) ||
                        event.getCategory().toLowerCase().contains(query))
                    mData.add(event);
            }
            presentData();

            if (events.size() <= 0) {
                events = new ArrayList<>(HomepageFragment.getEvents());
                delayNewQuery();
                Log.v(TAG, "delayed");
            }
    }

    //when the search is complete
    public void presentData() {
        adapter = new EventListItemAdapter(this, mData);
        mEventsView.setAdapter(adapter);
    }

    //this is in case it has not loaded yet
    public void delayNewQuery() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doTheSearch(mQuery);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SearchActivity.this.overridePendingTransition(
                R.anim.neg_left_right, R.anim.left_to_right);
    }

}

package com.ritwik.android.madfbla201415;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //item clicked not special
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init the data holders
        mData = new ArrayList<>();
        events = new ArrayList<>(HomepageFragment.getEvents());

        mEventsView = (ListView) findViewById(R.id.search_list_view);

        //handle the incoming android standard search intent
        Intent intent = getIntent();
        if ((intent.getAction()).equals(Intent.ACTION_SEARCH)) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.v(TAG, "success");
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
                        event.getmDetails().toLowerCase().contains(query))
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //TODO: BACK BUTTON
        
        return super.onOptionsItemSelected(item);
    }
}

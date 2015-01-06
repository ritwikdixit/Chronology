package com.ritwik.android.madfbla201415;

import android.app.SearchManager;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Iterator;


//Do not start this activity unless there is an intent with
//a search query. That is the sole purpose of this class.
public class SearchActivity extends ActionBarActivity{

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

        Log.v(TAG, "Activity Created! ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        events = new ArrayList<>(HomepageFragment.getEvents());

        mEventsView = (ListView) findViewById(R.id.search_list_view);

        Intent intent = getIntent();
        if ((intent.getAction()).equals(Intent.ACTION_SEARCH)) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.v(TAG, "success");
            doTheSearch(mQuery);
        }
    }

    public void doTheSearch(String query) {


        /*TODO: The Search! Read below
        The events array list already (should) have all the data of all the events
        there should not be any internet connecting, just query all the strings
        in all the EventItems in events.

        i.e. for (all EventItem 's in events):
            if any attribute of this EventItem (location, title, details) contains the query data
                then add it to the search results list (mData)

         */

        //putting in fake data
        int found = 0;
        Iterator<EventItem> iterator = events.iterator();
        while(found < TOTAL_TO_SEARCH && iterator.hasNext()){
            EventItem event = iterator.next();
            if(event.getmLocation().contains(query) || event.getmTitle().contains(query) || event.getmDetails().contains(query))
                mData.add(event);
        }
        presentData();
    }

    //when the search is complete
    public void presentData() {

        adapter = new EventListItemAdapter(this, mData);
        mEventsView.setAdapter(adapter);

    }

}

package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ritwik on 12/31/14.
 */
public class AllEventsActivity extends ActionBarActivity {

    private ListView mAllEventsView;
    private EventListItemAdapter adapter;
    private ArrayList<EventItem> events;

    private ImageButton mAllEventsButton;
    private ImageButton mHomeButton;
    private ImageButton mLogOutButton;

    private Activity mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevents);

        mAllEventsView =  (ListView) findViewById(R.id.all_events_listView);
        events = new ArrayList<>();
        Query eventsByDate = DataHolder.getRef().child("calendar").orderByChild("start_date");
        eventsByDate.addChildEventListener(new ChildEventListener() {

            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                events.add(new EventItem(
                        newEvent.get("start_date").toString(),
                        newEvent.get("end_date").toString(),
                        newEvent.get("start_time").toString(),
                        newEvent.get("end_time").toString(),
                        newEvent.get("title").toString(),
                        newEvent.get("location").toString(),
                        newEvent.get("details").toString()
                ));
                mAllEventsView.setAdapter(adapter);

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Loading Event Item Error", firebaseError.getMessage());
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
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmStartDate()));
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmEndDate()));
                detailIntent.putExtra(HomepageFragment.START_TIME_KEY,
                        events.get(position).getmStartTime());
                detailIntent.putExtra(HomepageFragment.END_TIME_KEY,
                        events.get(position).getmEndTime());
                detailIntent.putExtra(HomepageFragment.LOCATION_KEY,
                        events.get(position).getmLocation());
                detailIntent.putExtra(HomepageFragment.DETAILS_KEY,
                        events.get(position).getmDetails());

                startActivity(detailIntent);

            }
        });

        //bottom menu
        mAllEventsButton = (ImageButton) findViewById(R.id.all_events_menu_button);
        mHomeButton = (ImageButton) findViewById(R.id.homepage_button);
        mLogOutButton = (ImageButton) findViewById(R.id.log_out_button);
        initBottomMenu(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initBottomMenu(final Activity context) {

        //for all buttons if they are clicked go to appropriate activity

        mAllEventsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllEventsActivity.class);
                startActivity(intent);
            }
        });

        mHomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomepageActivity.class);
                startActivity(intent);
            }
        });
        mLogOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataHolder.getRef().unauth();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


}

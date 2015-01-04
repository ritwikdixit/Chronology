package com.ritwik.android.madfbla201415;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomepageFragment extends Fragment {

    //the scrolling image banner with a ViewPager, and adapter
    // essentially a list of images being animated by runnable

    private ViewPager mScrollBanner;
    private ListView mListView;
    private PagerAdapter mImageAdapter;
    private ProgressBar mProgressBar;

    private EventListItemAdapter eventAdapter;

    //this is static so i can refer to it in the other class
    //detail activity
    private static ArrayList<EventItem> events;

    //holds the IDs for the images, placeholder images
    private int[] mBannerIds = {
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher
    };

    private static final String LOG_TAG = "EventList";
    private Firebase ref = DataHolder.getRef();

    //keys for detail activity
    public static final String TITLE_KEY = "title";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String START_TIME_KEY = "start_time";
    public static final String END_TIME_KEY = "end_time";
    public static final String LOCATION_KEY = "location";
    public static final String DETAILS_KEY = "details";
    public static final String URL_KEY = "url";

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerArray = { "Month View",
            "All Events", "Home", "Search", "Log Out" };
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        //init the drawer
        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) rootView.findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_list_item, R.id.list_item_text, mDrawerArray));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
                getActivity(), mDrawerLayout, mDrawerList));

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //set all items unchecked
                for (int i = 0; i < mDrawerArray.length; i++) {
                    mDrawerList.setItemChecked(i, false);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.bringToFront();
        mDrawerLayout.requestLayout();

        mImageAdapter = new BannerAdapter(getActivity(), mBannerIds);
        mScrollBanner = (ViewPager) rootView.findViewById(R.id.scrolling_banner);
        mScrollBanner.setAdapter(mImageAdapter);

        //for aesthetics
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        //On Creation of Homepage, store user Data
        if(DataHolder.hasUserData())
            ref.child("users").child(DataHolder.getUID()).addChildEventListener(new ChildEventListener() {

                // Retrieve new posts as they are added to Firebase
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                    Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                    DataHolder.setEmail(newEvent.get("email").toString());
                    DataHolder.setName(newEvent.get("full_name").toString());
                    DataHolder.setPhoneNumber(newEvent.get("phone_number").toString());
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("Loading User Data Error", firebaseError.getMessage());
                }
            });

        //When a page changes on a banner the bar smooth scrolls to position
        mScrollBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //set the progress bar accordingly
                int bar = (int) (position *
                        (double) mProgressBar.getMax() / (mBannerIds.length - 1) + 0.5);
                ObjectAnimator anim = ObjectAnimator.ofInt(mProgressBar, "progress", bar);
                anim.setDuration(350);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }
        });

        //TODO: WRAP BACK ON SWIPE

        mListView = (ListView) rootView.findViewById(R.id.list_view);

        events = new ArrayList<EventItem>();
        Query eventsByDate = ref.child("calendar").orderByChild("start_date");
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
                        newEvent.get("details").toString(),
                        newEvent.get("url").toString()
                ));
                mListView.setAdapter(eventAdapter);
                Log.d("", newEvent.get("url").toString());

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


        eventAdapter
                = new EventListItemAdapter(getActivity(), events);

        mListView.setAdapter(eventAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent to detail activity with position extra and data
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, position + 1);

                detailIntent.putExtra(TITLE_KEY, events.get(position).getmTitle());
                detailIntent.putExtra(START_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmStartDate()));
                detailIntent.putExtra(END_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmEndDate()));
                detailIntent.putExtra(START_TIME_KEY, events.get(position).getmStartTime());
                detailIntent.putExtra(END_TIME_KEY, events.get(position).getmEndTime());
                detailIntent.putExtra(LOCATION_KEY, events.get(position).getmLocation());
                detailIntent.putExtra(DETAILS_KEY, events.get(position).getmDetails());
                detailIntent.putExtra(URL_KEY, events.get(position).getmUrl());

                startActivity(detailIntent);

            }
        });

        return rootView;
    }

}

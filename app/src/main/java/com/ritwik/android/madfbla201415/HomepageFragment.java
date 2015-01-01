package com.ritwik.android.madfbla201415;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class HomepageFragment extends Fragment {

    //the scrolling image banner with a ViewPager, and adapter
    // essentially a list of images being animated by runnable

    private ViewPager mScrollBanner;
    private ListView mListView;
    private PagerAdapter mImageAdapter;
    private ProgressBar mProgressBar;

    private ImageButton mAllEventsButton;

    private EventListItemAdapter eventAdapter;
    private ArrayList<EventItem> events;

    //holds the IDs for the images, placeholder images
    private int[] mBannerIds = {
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher
    };

    private static final String LOG_TAG = "EventList";
    private Firebase ref = DataHolder.getRef();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        mImageAdapter = new BannerAdapter(getActivity(), mBannerIds);
        mScrollBanner = (ViewPager) rootView.findViewById(R.id.scrolling_banner);
        mScrollBanner.setAdapter(mImageAdapter);

        mAllEventsButton = (ImageButton) rootView.findViewById(R.id.all_events_menu_button);

        //for aesthetics
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

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
                        newEvent.get("details").toString()
                ));
                mListView.setAdapter(eventAdapter);

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

        mAllEventsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AllEventsActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }




}

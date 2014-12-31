package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    //holds the IDs for the images, placeholder images
    private int[] mBannerIds = {
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher
    };

    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private Firebase ref = new Firebase(URL_FIREBASE);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        mImageAdapter = new BannerAdapter(getActivity(), mBannerIds);
        mScrollBanner = (ViewPager) rootView.findViewById(R.id.scrolling_banner);
        mScrollBanner.setAdapter(mImageAdapter);

        mListView = (ListView) rootView.findViewById(R.id.list_view);

        final ArrayList<EventItem> events = new ArrayList<EventItem>();
        ref.child("calendar").addChildEventListener(new ChildEventListener() {
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


        //TODO: THe problem is that if you go to event item class
        //the data isnt done downloading because it creates "listeners"
        //which can go off at any time so the code skips over them

        EventListItemAdapter eventAdapter
                = new EventListItemAdapter(getActivity(), events);

        mListView.setAdapter(eventAdapter);


        return rootView;
    }



}

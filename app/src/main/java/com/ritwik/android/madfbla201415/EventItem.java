package com.ritwik.android.madfbla201415;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EventItem {

    private static final String LOG_TAG = "EventList";

    private String mStartDate;
    private String mEndDate;

    private String mStartTime;
    private String mEndTime;

    private String mTitle;
    private String mLocation;
    private String mDetails;

    public boolean done = false;

    public EventItem(Firebase eventRef) {


        //TODO: this constructor executes quickly
        //TODO: but all the fields still are null for about 2 seconds
        //TODO: until the data loads though the listeners

        eventRef.child("start_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mStartDate = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("end_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mEndDate = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("start_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mStartTime = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("end_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mEndTime = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mTitle = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mLocation = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
            }
        });

        eventRef.child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mDetails = snapshot.getValue().toString();
                done = true;
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(LOG_TAG, firebaseError.getMessage());
                done = true;
            }
        });



    }


    public String getmStartDate() {
        return mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDetails() {
        return mDetails;
    }



}

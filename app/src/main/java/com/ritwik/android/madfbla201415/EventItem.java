package com.ritwik.android.madfbla201415;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class EventItem {

    private static final String LOG_TAG = "EventList";

    private String mStartDate;
    private String mEndDate;

    private String mStartTime;
    private String mEndTime;

    private String mTitle;
    private String mLocation;
    private String mDetails;

    private String mUrl;

    public EventItem(String mStartDate, String mEndDate, String mStartTime,
                     String mEndTime, String mTitle, String mLocation, String mDetails,
                     String mUrl) {

        this.mTitle = mTitle;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this. mLocation = mLocation;
        this.mDetails = mDetails;
        this.mUrl = mUrl;
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

    public String getmUrl() { return mUrl; }

    //formats date so it fits in the listView

    public String formatDate(String mServerDateData) {
        String[] parts = mServerDateData.split("-");

        return theMonth(Integer.parseInt(parts[1])) + " " + parts[2] + ", " + parts[0];
    }
    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }
}

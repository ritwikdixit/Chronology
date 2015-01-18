package com.ritwik.android.madfbla201415;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class EventItem {

    private String id;
    private String mStartDate;
    private String mEndDate;

    private String mStartTime;
    private String mEndTime;

    private String mTitle;
    private String mLocation;
    private String mDetails;

    private String mUrl;
    private ImageView mImage;
    private String mContactInfo;

    public EventItem(String id, String mStartDate, String mEndDate, String mStartTime,
                     String mEndTime, String mTitle, String mLocation, String mDetails,
                     String mUrl, String mContactInfo, Context context) {
        this.id = id;
        this.mTitle = mTitle;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this. mLocation = mLocation;
        this.mDetails = mDetails;
        this.mUrl = mUrl;
        this.mContactInfo = mContactInfo;

        mImage = new ImageView(context);
        mImage.setImageResource(R.drawable.load_horiz_anim);
        AnimationDrawable loadAnimation = (AnimationDrawable) mImage.getDrawable();
        loadAnimation.start();

         new HomepageFragment.DownloadImageTask(mImage).execute(this.mUrl);
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

    public ImageView getmImage() {
        return mImage;
    }

    public String getmContactInfo() {
        return mContactInfo;
    }

    public String getId() {return id;}


    //formats date so it fits in the listView

    public static String formatDate(String mServerDateData) {
        String[] parts = mServerDateData.split("-");
        return theMonth(Integer.parseInt(parts[1])) + " " + parts[2] + ", " + parts[0];
    }
    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "June",
                "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }


    //this is for debugging do not delete
    @Override
    public String toString() {
        return mTitle + "---"
                + mStartDate + "-" + mEndDate
                + " " + mStartTime + "-" + mEndTime
                + " locate @" + mLocation + " " + " details=" + mDetails
                + " img src=" + mUrl + " contact @" + mContactInfo;

    }
}

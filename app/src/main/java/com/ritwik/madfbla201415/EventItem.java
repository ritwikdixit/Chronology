package com.ritwik.madfbla201415;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.ritwik.android.madfbla201415.R;

import java.io.Serializable;
import java.util.Date;

public class EventItem implements Serializable{

    private String number;
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
    private String category;

    private boolean isAttending;

    public EventItem(String number, String id, String mStartDate, String mEndDate, String mStartTime,
                     String mEndTime, String mTitle, String mLocation, String mDetails,
                     String mUrl, String mContactInfo, String category, boolean attending,
                     Context context) {

        this.number = number;
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
        this.category = category;
        this.isAttending = attending;

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

    public String getCategory() {
        return category;
    }

    public String getNumber() {
        return number;
    }

    public boolean isAttending() {
        return isAttending;
    }

    public void setAttending(boolean set) {
        this.isAttending = set;
    }

    //formats date so it fits in the listView

    public static String formatDate(String mServerDateData) {
        String[] parts = mServerDateData.split("-");
        return theMonth(Integer.parseInt(parts[1])) + " " + parts[2] + ", " + parts[0];
    }

    public static String formatDateCondense(String mServerDateData) {
        String[] parts = mServerDateData.split("-");
        return theMonth(Integer.parseInt(parts[1])) + " " + parts[2];
    }

    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "June",
                "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }

    public Date dateStart(){
        String[] sd = getmStartDate().split("-");
        Date date  = new Date();
        date.setMonth(Integer.parseInt(sd[1]));
        date.setDate(Integer.parseInt(sd[2]));
        date.setYear(Integer.parseInt(sd[0]) - 1900);
        return date;
    }
    public Date dateStart(boolean y){
        String[] sd = getmStartDate().split("-");
        Date date  = new Date();
        date.setMonth(Integer.parseInt(sd[1]) - 1);
        if(y)
            date.setDate(Integer.parseInt(sd[2]) - 1);
        else
            date.setDate(Integer.parseInt(sd[2]));
        date.setYear(Integer.parseInt(sd[0]) - 1900);
        return date;
    }
    public Date dateEnd(){
        String[] sd = getmEndDate().split("-");
        Date date  = new Date();
        date.setMonth(Integer.parseInt(sd[1]));
        date.setDate(Integer.parseInt(sd[2]));
        date.setYear(Integer.parseInt(sd[0]) - 1900);
        return date;
    }
    public Date dateEnd(boolean x){
        String[] sd = getmEndDate().split("-");
        Date date  = new Date();
        date.setMonth(Integer.parseInt(sd[1]) - 1);
        if(x)
            date.setDate(Integer.parseInt(sd[2]) - 1);
        else
            date.setDate(Integer.parseInt(sd[2]));
        date.setYear(Integer.parseInt(sd[0]) - 1900);
        return date;
    }

    //this is for debugging do not delete
    @Override
    public String toString() {
        return mTitle + "---"
                + mStartDate + "-" + mEndDate
                + " " + mStartTime + "-" + mEndTime
                + " locate @" + mLocation + " " + " details=" + mDetails
                + " img src=" + mUrl + " contact @" + mContactInfo
                + " @category " + category;

    }
}

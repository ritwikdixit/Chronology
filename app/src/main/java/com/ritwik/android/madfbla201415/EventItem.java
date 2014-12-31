package com.ritwik.android.madfbla201415;


public class EventItem {

    private static final String LOG_TAG = "EventList";

    private String mStartDate;
    private String mEndDate;

    private String mStartTime;
    private String mEndTime;

    private String mTitle;
    private String mLocation;
    private String mDetails;

    public EventItem(String mStartDate, String mEndDate, String mStartTime,
                     String mEndTime, String mTitle, String mLocation, String mDetails) {

        this.mTitle = mTitle;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this. mLocation = mLocation;
        this.mDetails = mDetails;

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

    //formats date so it fits in the listView

    public String formatDate(String mServerDateData) {
        String[] parts = mServerDateData.split("-");
        return parts[1] + "/" + parts[2] + "/" + parts[0];

    }

}

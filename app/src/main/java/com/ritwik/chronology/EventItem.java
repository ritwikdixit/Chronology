package com.ritwik.chronology;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.ritwik.chronology.HomepageFragment.DownloadImageTask;

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

    private DownloadImageTask downloader;

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

        downloader = new DownloadImageTask(mImage);
        downloader.execute(this.mUrl);
    }

    //internally saved data
    public EventItem(String[] data, Context context) {
        this(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                data[8], data[9], data[10], data[11], Boolean.parseBoolean(data[12]), context);
    }

    //onRotate
    public void contextsUpdate(Context context) {
        mImage = new ImageView(context);
        mImage.setImageResource(R.drawable.load_horiz_anim);
        AnimationDrawable loadAnimation = (AnimationDrawable) mImage.getDrawable();
        loadAnimation.start();
        downloader.updateImageView(mImage);
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

    public DownloadImageTask getDownloader() {
        return downloader;
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

    //year - 1900, month - 1, day
    public Date getStartDateObject() {
        String[] components = getmStartDate().split("-");
        Date date  = new Date(
            Integer.parseInt(components[0]) - 1900,
            Integer.parseInt(components[1]) - 1,
            Integer.parseInt(components[2]));
        return date;
    }

    public Date getEndDateObject() {
        String[] components = getmEndDate().split("-");
        Date date  = new Date(
                Integer.parseInt(components[0]) - 1900,
                Integer.parseInt(components[1]) - 1,
                Integer.parseInt(components[2]));
        return date;
    }

    public ArrayList<Date> getAllDatesBetweenStartAndEnd() {
        ArrayList<Date> dates = new ArrayList<>();

        //add all dates within to the list
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartDateObject());
        while (cal.getTime().compareTo(getEndDateObject()) < 1) {
            dates.add(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }

        return dates;
    }

    //this is for debugging do not delete
    @Override
    public String toString() {
        return
                number + "---" + id
                + "---" +mStartDate + "---" + mEndDate
                + "---" + mStartTime + "---" + mEndTime
                +  "---" + mTitle
                + "---" + mLocation + " ---" + mDetails
                + "---" + mUrl + "---" + mContactInfo
                + "---" + category + "---" + isAttending + "<*>";

    }
}

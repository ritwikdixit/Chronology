package com.ritwik.android.madfbla201415;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.CalendarContract.Events;

import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DetailActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mLocation;
    private TextView mDetails;
    private TextView mContactInfo;
    private TextView mTitle;
    private TextView mCategory;

    private LinearLayout layout;

    private String imageUrl;
    private String titleString, startDateStr, endDateStr,
            startTimeStr, endTimeStr, locationStr, descStr;
    private boolean isRSVPtoEvent;
    private ImageView mImage;

    private static final String LOG_TAG = "EventList";
    private Firebase ref;
    private EventItem thisEvent;

    private GestureDetectorCompat mLeftDetector;
    private View.OnTouchListener mListener;
    private SwipeListener mFlinglistener;

    private ShareActionProvider mShare;
    private String eventNum;

    private Toolbar toolbar;
    private SearchView mSearch;
    private Button mCalendarButton, mRSVPButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_new);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTitle = (TextView) findViewById(R.id.title2);
        mStartDate = (TextView) findViewById(R.id.detail_start_date);
        mEndDate = (TextView) findViewById(R.id.detail_end_date);
        mStartTime = (TextView) findViewById(R.id.detail_start_time);
        mEndTime = (TextView) findViewById(R.id.detail_end_time);
        mLocation = (TextView) findViewById(R.id.detail_location);
        mDetails = (TextView) findViewById(R.id.detail_details);
        mImage = (ImageView) findViewById(R.id.detail_image);
        mContactInfo = (TextView) findViewById(R.id.detail_contact_info);
        mCategory = (TextView) findViewById(R.id.details_category);
        mRSVPButton = (Button) findViewById(R.id.rsvp_button);

        Firebase.setAndroidContext(this);
        ref = DataHolder.getRef();

        //if this was the 4th event, you could get fire base event called event4
        eventNum = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        //setting the action bar label to title of event
        titleString = getIntent().getStringExtra(HomepageFragment.TITLE_KEY);
        mTitle.setText(titleString);

        startDateStr = getIntent().getStringExtra(HomepageFragment.START_DATE_KEY);
        endDateStr = getIntent().getStringExtra(HomepageFragment.END_DATE_KEY);
        startTimeStr = getIntent().getStringExtra(HomepageFragment.START_TIME_KEY);
        endTimeStr = getIntent().getStringExtra(HomepageFragment.END_TIME_KEY);
        locationStr = getIntent().getStringExtra(HomepageFragment.LOCATION_KEY);
        descStr = getIntent().getStringExtra(HomepageFragment.DETAILS_KEY);
        isRSVPtoEvent = getIntent().getBooleanExtra(HomepageFragment.RSVP_KEY, false);

        if (isRSVPtoEvent) {
            Log.v(HomepageFragment.LOG_TAG, "It works until detail activity onCreate");
            mRSVPButton.setText("Cancel RSVP");
        }

        //getting the extras from the intent and putting them in the view
        mStartDate.setText("Date: " + EventItem.formatDate(startDateStr));
        mEndDate.setText(EventItem.formatDate(endDateStr));
        mStartTime.setText("Time: " + startTimeStr);
        mEndTime.setText(endTimeStr);
        mLocation.setText("Location: " + locationStr);
        mDetails.setText(descStr);
        mContactInfo.setText("Contact: " +
                getIntent().getStringExtra(HomepageFragment.CONTACT_INFO_KEY)
        );

        mCategory.setText(getIntent().getStringExtra(HomepageFragment.CATEGORY_KEY));

        // This is the image URL
        imageUrl = getIntent().getStringExtra(HomepageFragment.URL_KEY);

        // ImageView setup with animation background and picture
        mImage.setImageResource(R.drawable.load_horiz_anim);
        AnimationDrawable loadAnimation = (AnimationDrawable) mImage.getDrawable();
        loadAnimation.start();

        new HomepageFragment.DownloadImageTask(mImage).execute(imageUrl);

        layout = (LinearLayout) findViewById(R.id.root_container_detail);

        mFlinglistener = new SwipeListener();
        mLeftDetector = new GestureDetectorCompat(this, mFlinglistener);
        mListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touch = mLeftDetector.onTouchEvent(event);
                if (mFlinglistener.hasSwiped()) {
                    finish();
                    DetailActivity.this.overridePendingTransition(
                            R.anim.neg_left_right, R.anim.left_to_right);
                }
                return touch;
            }
        };

        layout.setOnClickListener(DetailActivity.this);
        layout.setOnTouchListener(mListener);

        mCalendarButton = (Button) findViewById(R.id.add_event_to_calendar_button);
        mCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendarIntent();
            }
        });

        mRSVPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rsvpToEvent();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        //android library API 11+ standard search
        SearchManager managerSearch = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.chronology_search_bar).getActionView();
        mSearch.setSearchableInfo(managerSearch.getSearchableInfo(getComponentName()));
        mSearch.setIconifiedByDefault(true);

        mShare = (ShareActionProvider) MenuItemCompat
                .getActionProvider(menu.findItem(R.id.chronology_share_action));

        mShare.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    public void rsvpToEvent() {

        if (isRSVPtoEvent) {
            RSVP.rsvp(eventNum, RSVP.NOT_GOING);
            mRSVPButton.setText("You have Cancelled your RSVP");
            isRSVPtoEvent = false;
        } else {
            RSVP.rsvp(eventNum, RSVP.GOING);
            mRSVPButton.setText("You have RSVP'd");
            isRSVPtoEvent = true;
        }

        HomepageFragment.getEvents().get(HomepageFragment
                .getEventsPositionForNumQuery(eventNum)).setAttending(isRSVPtoEvent);
        HomepageFragment.extendsToday();
    }

    public void callCalendarIntent() {

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        int[] beg = parseDateTime(startDateStr, startTimeStr);
        int[] end = parseDateTime(endDateStr, endTimeStr);

        beginTime.set(beg[0], beg[1], beg[2], beg[3], beg[4]);
        endTime.set(end[0], end[1], end[2], end[3], end[4]);

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, titleString)
                .putExtra(Events.DESCRIPTION, descStr + " " + mContactInfo.getText())
                .putExtra(Events.EVENT_LOCATION, locationStr)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

        startActivity(calendarIntent);

    }

    //function to parse into something the Intent will understand
    public int[] parseDateTime(String date, String time) {

        int hour = Integer.valueOf(time.substring(0, time.indexOf(":")));
        int minutes = Integer.valueOf(time.substring(time.indexOf(":") + 1, time.length() - 2));

        String[] data = date.split("-");

        int year = Integer.valueOf(data[0]);
        int month = Integer.valueOf(data[1]) - 1;
        int day = Integer.valueOf(data[2]);

        if (time.contains("PM") && hour != 12)
            hour += 12;

        Log.v(SettingsActivity.SQL_LOG_TAG, year + " " + month + "  " + day
                + " /time/ " + hour + " " + minutes);

        return new int[] { year, month, day, hour, minutes };

    }

    private Intent getDefaultIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "I would like to share the event: " + titleString + " via Chronology!");
        return shareIntent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailActivity.this.overridePendingTransition(
                R.anim.neg_left_right, R.anim.left_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.chronology_search_bar
                || id == R.id.chronology_share_action) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }


}

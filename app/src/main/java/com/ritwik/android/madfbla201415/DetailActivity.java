package com.ritwik.android.madfbla201415;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.InputStream;

public class DetailActivity extends ActionBarActivity {

    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mLocation;
    private TextView mDetails;
    private TextView mContactInfo;

    private String imageUrl;
    private ImageView mImage;

    private static final String LOG_TAG = "EventList";
    private Firebase ref;
    private EventItem thisEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mStartDate = (TextView) findViewById(R.id.detail_start_date);
        mEndDate = (TextView) findViewById(R.id.detail_end_date);
        mStartTime = (TextView) findViewById(R.id.detail_start_time);
        mEndTime = (TextView) findViewById(R.id.detail_end_time);
        mLocation = (TextView) findViewById(R.id.detail_location);
        mDetails = (TextView) findViewById(R.id.detail_details);
        mImage = (ImageView) findViewById(R.id.detail_image);
        mContactInfo = (TextView) findViewById(R.id.detail_contact_info);

        Firebase.setAndroidContext(this);
        ref = DataHolder.getRef();

        //if this was 4 you could get fire base event called event4
        int eventNum = getIntent().getIntExtra(Intent.EXTRA_TEXT, 1);

        //setting the action bar label to title of event
        setTitle(getIntent().getStringExtra(HomepageFragment.TITLE_KEY));

        //getting the extras from the intent and putting them in the view
        mStartDate.setText("Date: " +
                getIntent().getStringExtra(HomepageFragment.START_DATE_KEY));
        mEndDate.setText(
                getIntent().getStringExtra(HomepageFragment.END_DATE_KEY));
        mStartTime.setText("Time: " +
                getIntent().getStringExtra(HomepageFragment.START_TIME_KEY));
        mEndTime.setText(
                getIntent().getStringExtra(HomepageFragment.END_TIME_KEY));
        mLocation.setText("Location: " +
                getIntent().getStringExtra(HomepageFragment.LOCATION_KEY));
        mDetails.setText(
                getIntent().getStringExtra(HomepageFragment.DETAILS_KEY));
        mContactInfo.setText("Contact: " +
                getIntent().getStringExtra(HomepageFragment.CONTACT_INFO_KEY)
        );

        // This is the image URL
        imageUrl = getIntent().getStringExtra(HomepageFragment.URL_KEY);

        // ImageView setup with animation background and picture
        mImage.setImageResource(R.drawable.load_horiz_anim);
        AnimationDrawable loadAnimation = (AnimationDrawable) mImage.getDrawable();
        loadAnimation.start();

        new HomepageFragment.DownloadImageTask(mImage).execute(imageUrl);
        //mImage = HomepageFragment.getEvents().get(eventNum - 1).getmImage();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

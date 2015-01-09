package com.ritwik.android.madfbla201415;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class DetailActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mLocation;
    private TextView mDetails;
    private TextView mContactInfo;

    private LinearLayout layout;

    private String imageUrl;
    private ImageView mImage;

    private static final String LOG_TAG = "EventList";
    private Firebase ref;
    private EventItem thisEvent;

    private GestureDetectorCompat mLeftDetector;
    private View.OnTouchListener mListener;
    private SwipeListener mFlinglistener;

    private ShareActionProvider mShare;

    private Toolbar toolbar;
    private SearchView mSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Chronology is so good!");
        return intent;
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

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.CalendarContract.Events;

import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

// joshua zhou
public class HelpActivity extends ActionBarActivity {

    private TextView mContents;

    private LinearLayout layout;
    private ListView features;

    private static final String LOG_TAG = "Help";
    private Firebase ref;

    private GestureDetectorCompat mLeftDetector;
    private View.OnTouchListener mListener;
    private SwipeListener mFlinglistener;

    private ShareActionProvider mShare;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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

        mContents = (TextView)findViewById(R.id.help_contents);
        List<String> featureStrings = Arrays.asList(getResources().getStringArray(R.array.features));
        mContents.setText(mContents.getText() + "\n");
        for (String feature : featureStrings) {
            Log.d(LOG_TAG, "" + (mContents == null));
            mContents.setText(mContents.getText() + "\n •  " + feature);
        }


        Firebase.setAndroidContext(this);
        ref = DataHolder.getRef();

        //if this was the 4th event, you could get fire base event called event4
        //int eventNum = getIntent().getIntExtra(Intent.EXTRA_TEXT, 1);

        layout = (LinearLayout) findViewById(R.id.help_root);

        mFlinglistener = new SwipeListener();
        mLeftDetector = new GestureDetectorCompat(this, mFlinglistener);
        mListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touch = mLeftDetector.onTouchEvent(event);
                if (mFlinglistener.hasSwiped()) {
                    finish();
                    HelpActivity.this.overridePendingTransition(
                            R.anim.neg_left_right, R.anim.left_to_right);
                }
                return touch;
            }
        };

        Log.d(LOG_TAG, layout + "," + mListener);
        layout.setOnTouchListener(mListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HelpActivity.this.overridePendingTransition(
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


}

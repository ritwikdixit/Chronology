package com.ritwik.android.madfbla201415;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.CalendarContract.Events;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

// joshua zhou
public class HelpActivity extends ActionBarActivity {

    // add fragments for features
    private Fragment[] features = new Fragment[] {
            HelpScreenSlidePageFragment.instance(
                    "Calendar",
                    "View events on a calendar. The days with events are indicated. " +
                    "You can also sync events to Google Calendar " +
                            "and share events.",
                    R.drawable.sc_cal
            ),


            HelpScreenSlidePageFragment.instance(
                    "Search",
                    "search events",
                    R.drawable.sc_cal
            )
    };

    private ViewPager mPager;
    private static final String LOG_TAG = "Help";

    private GestureDetectorCompat mLeftDetector;
    private View.OnTouchListener mListener;
    private SwipeListener mFlinglistener;
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

//        layout = (LinearLayout) findViewById(R.id.help_root);

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

        mPager = (ViewPager) findViewById(R.id.pager);
        List<Fragment> fragments = getFragments();
        mPager.setAdapter(
                new ScreenSlidePagerAdapter(
                        getSupportFragmentManager(),
                        fragments
                )
        );

    }

    private List<Fragment> getFragments() {
        Bundle b1 = new Bundle();
        b1.putString("name", "abc");

        Bundle b2 = new Bundle();
        b2.putString("name", "faaaaafooo");

        Bundle b3 = new Bundle();
        b3.putString("name", "egggg");

        Fragment f1 = new HelpScreenSlidePageFragment();
        f1.setArguments(b1);

        Fragment f2 = new HelpScreenSlidePageFragment();
        f2.setArguments(b2);

        Fragment f3 = new HelpScreenSlidePageFragment();
        f2.setArguments(b3);

        return Arrays.asList(new Fragment[] { f1, f2, f3 });
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
        this.finish();
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

    // Slide pager
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {

            return features[i];
        }

        @Override
        public int getCount() {
            return features.length;
        }

        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}

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

    private int itemsPer = 2;
    private List<String> features = Arrays.asList(new String[] {
            "Calendar", "check cal",
            "Search", "search events",
            "Help", "help thangs",
    });

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
        mPager.setAdapter(new ScreenSlidePagerAdapter(
                        getSupportFragmentManager(), fragments)
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
        // go back through slides. if at front, go back to whatever you came from
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
            HelpActivity.this.overridePendingTransition(
                    R.anim.neg_left_right, R.anim.left_to_right);
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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

            return HelpScreenSlidePageFragment.instance(
                    features.get(itemsPer * i),
                    features.get(itemsPer * i + 1)
            );
        }

        @Override
        public int getCount() {
            return features.size() / itemsPer;
        }

        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


}

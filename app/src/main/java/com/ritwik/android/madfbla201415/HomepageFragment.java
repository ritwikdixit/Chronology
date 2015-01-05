package com.ritwik.android.madfbla201415;

import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomepageFragment extends Fragment {

    //the scrolling image banner with a ViewPager, and adapter
    // essentially a list of images being animated by runnable

    private ViewPager mScrollBanner;
    private ListView mListView;
    private PagerAdapter mImageAdapter;
    private ProgressBar mProgressBar;

    private EventListItemAdapter eventAdapter;

    //this is static so i can refer to it in the other class
    //detail activity
    private static ArrayList<EventItem> events;

    private static final String LOG_TAG = "EventList";
    private Firebase ref = DataHolder.getRef();

    //keys for detail activity
    public static final String TITLE_KEY = "title";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String START_TIME_KEY = "start_time";
    public static final String END_TIME_KEY = "end_time";
    public static final String LOCATION_KEY = "location";
    public static final String DETAILS_KEY = "details";
    public static final String URL_KEY = "url";
    public static final String CONTACT_INFO_KEY = "contact_info";

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerArray = { "Month View",
            "All Events", "Home", "Search", "Log Out" };
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private SearchView mSearch;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager managerSearch = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);

        MenuItem item = menu.findItem(R.id.chronology_search_bar);
        mSearch = (SearchView) MenuItemCompat.getActionView(item);

        mSearch.setSearchableInfo(managerSearch.getSearchableInfo(getActivity().getComponentName()));
        mSearch.setIconifiedByDefault(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.chronology_search_bar
                || item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });

        //init the drawer
        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) rootView.findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_list_item, R.id.list_item_text, mDrawerArray));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
                getActivity(), mDrawerLayout, mDrawerList));

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout,  toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //set all items unchecked
                for (int i = 0; i < mDrawerArray.length; i++) {
                    mDrawerList.setItemChecked(i, false);
                }
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.bringToFront();
        mDrawerLayout.requestLayout();

        //for aesthetics
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
        //On Creation of Homepage, store user Data
        if(DataHolder.hasUserData())
            ref.child("users").child(DataHolder.getUID()).addChildEventListener(new ChildEventListener() {

                // Retrieve new posts as they are added to Firebase
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                    Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                    DataHolder.setEmail(newEvent.get("email").toString());
                    DataHolder.setName(newEvent.get("full_name").toString());
                    DataHolder.setPhoneNumber(newEvent.get("phone_number").toString());
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("Loading User Data Error", firebaseError.getMessage());
                }
            });

        mListView = (ListView) rootView.findViewById(R.id.list_view);

        events = new ArrayList<EventItem>();
        Query eventsByDate = ref.child("calendar").orderByChild("start_date");
        eventsByDate.addChildEventListener(new ChildEventListener() {

            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                events.add(new EventItem(
                        newEvent.get("start_date").toString(),
                        newEvent.get("end_date").toString(),
                        newEvent.get("start_time").toString(),
                        newEvent.get("end_time").toString(),
                        newEvent.get("title").toString(),
                        newEvent.get("location").toString(),
                        newEvent.get("details").toString(),
                        newEvent.get("url").toString(),
                        newEvent.get("contact_info").toString(),
                        getActivity()
                ));

                mListView.setAdapter(eventAdapter);
                mImageAdapter = new BannerAdapter(getActivity(), events);
                mScrollBanner.setAdapter(mImageAdapter);

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Loading Event Item Error", firebaseError.getMessage());
            }
        });

        //Banner Adapter
        mImageAdapter = new BannerAdapter(getActivity(), events);
        mScrollBanner = (ViewPager) rootView.findViewById(R.id.scrolling_banner);
        mScrollBanner.setAdapter(mImageAdapter);

        //When a page changes on a banner the bar smooth scrolls to position
        mScrollBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //set the progress bar accordingly
                int bar = (int) (position *
                        (double) mProgressBar.getMax() / (events.size() - 1) + 0.5);
                ObjectAnimator anim = ObjectAnimator.ofInt(mProgressBar, "progress", bar);
                anim.setDuration(350);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }

        });

        eventAdapter
                = new EventListItemAdapter(getActivity(), events);

        mListView.setAdapter(eventAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent to detail activity with position extra and data
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, position + 1);

                detailIntent.putExtra(TITLE_KEY, events.get(position).getmTitle());
                detailIntent.putExtra(START_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmStartDate()));
                detailIntent.putExtra(END_DATE_KEY, events.get(position)
                        .formatDate(events.get(position).getmEndDate()));
                detailIntent.putExtra(START_TIME_KEY, events.get(position).getmStartTime());
                detailIntent.putExtra(END_TIME_KEY, events.get(position).getmEndTime());
                detailIntent.putExtra(LOCATION_KEY, events.get(position).getmLocation());
                detailIntent.putExtra(DETAILS_KEY, events.get(position).getmDetails());
                detailIntent.putExtra(URL_KEY, events.get(position).getmUrl());
                detailIntent.putExtra(CONTACT_INFO_KEY, events.get(position).getmContactInfo());

                HomepageFragment.this.setEnterTransition(R.anim.right_to_left);
                HomepageFragment.this.setExitTransition(R.anim.neg_right_left);
                startActivity(detailIntent);

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public static ArrayList<EventItem> getEvents() {
        return events;
    }


    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

            //only do this if they dont already have the image

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Bitmap", e.getMessage() + " " + e.getLocalizedMessage() + " error");
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.v("Bitmap", "Done Loading Bitmap");
            bmImage.setImageBitmap(result);

        }

    }
}

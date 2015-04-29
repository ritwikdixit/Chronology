package com.ritwik.chronology;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.ritwik.chronology.Push.PushActivity;
import com.ritwik.chronology.Push.PushReceiver;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

/* Note: if the app is not launched to this page (ie notification) other pages may
 * not work properly or the app may crash due to uninitialized data and nulls everywhere
 */

public class HomepageFragment extends Fragment {

    private static Activity activity;

    //the scrolling image banner with a ViewPager, and adapter
    // essentially a list of images being animated by runnable

    private ViewPager mScrollBanner;
    private ListView mListView;
    private PagerAdapter mImageAdapter;
    private ProgressBar mProgressBar;
    private LinearLayout mScrollerLayout;

    private EventListItemAdapter eventAdapter;
    private View thisRootView;


    //NOTE: if this is not initialized, app crashes on event create
    //that will happen if the app is launched and homepage fragment onCreateView never called
    private static Activity parentActivity;

    //this is static so i can refer to it in the other class
    //detail activity
    private static ArrayList<EventItem> events;
    private static ArrayList<EventItem> showEvents;

    public static final String LOG_TAG = "EventList";
    public static final String DATA_TAG = "DataStorage";

    private Firebase ref = DataHolder.getRef();

    //keys for detail activity
    public static final String ID_KEY = "id";
    public static final String TITLE_KEY = "title";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String START_TIME_KEY = "start_time";
    public static final String END_TIME_KEY = "end_time";
    public static final String LOCATION_KEY = "location";
    public static final String DETAILS_KEY = "details";
    public static final String URL_KEY = "url";
    public static final String CONTACT_INFO_KEY = "contact_info";
    public static final String CATEGORY_KEY = "category";
    public static final String RSVP_KEY = "rsvp";

    public static final String ARRAY_DATA_KEY = "data_array";

    public static final String CAT_SPORTS_KEY = "Sports";
    public static final String CAT_HOLIDAY_KEY = "Holiday";
    public static final String CAT_CLUB_KEY = "Clubs";
    public static final String CAT_FUN_KEY = "Fun Event";
    public static final String CAT_ACADEMICS_KEY = "Academics";

    public static final String EVENTS_FILENAME = "Events_File_Name";

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private SearchView mSearch;

    private static Comparator eventCompare;
    private static boolean rotatedScreen = false;
    private boolean getData = true;
    private int instanceID = 0;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);

        //android library API 11+ standard search
        SearchManager managerSearch = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.chronology_search_bar).getActionView();
        mSearch.setSearchableInfo(
                managerSearch.getSearchableInfo(getActivity().getComponentName()));
        mSearch.setIconifiedByDefault(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.chronology_search_bar) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        parentActivity = getActivity();
        instanceID = (int)(Math.random() * 1000000);

        //init the drawer
        thisRootView = rootView;
        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) rootView.findViewById(R.id.left_drawer);
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
                for (int i = 0; i < DataHolder.getDrawerArray().length; i++) {
                    mDrawerList.setItemChecked(i, false);
                }
            }
        };

        initDrawer(parentActivity);
        //for aesthetics
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        //On Creation of Homepage, store user Data
        if(!DataHolder.hasUserData())
            Log.v(LOG_TAG, "Yeah Admin works this timex");
            ref.child("users").child(DataHolder.getUID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> newUser = (Map<String, Object>) dataSnapshot.getValue();
                        DataHolder.setEmail(newUser.get("email").toString());
                        DataHolder.setName(newUser.get("full_name").toString());
                        DataHolder.setPhoneNumber(newUser.get("phone_number").toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


        ref.child("admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> adminUsers = (Map<String, Object>) snapshot.getValue();
                if (adminUsers.containsKey(DataHolder.getUID()))
                    DataHolder.setAdmin(true);
                initDrawer(parentActivity);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, firebaseError.getDetails());
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mScrollerLayout = (LinearLayout) rootView.findViewById(R.id.scroller_layout);

        if (!isNetworkAvailable()) {
            events = DataStorage.readFromFile(parentActivity);
            showEvents = new ArrayList<>();
        }

        /*
        Short circuit on Java OR makes this work!
         */
        if (events == null || events.size() == 0/* || rotatedScreen*/) {

            //implementing the custom comparator to sort events
            eventCompare = new Comparator<EventItem>() {

                @Override
                public int compare(EventItem event, EventItem event2) {
                    return event.getmStartDate().compareTo(event2.getmStartDate());
                }
            };

            //clear all events
            events = new ArrayList<>();
            showEvents = new ArrayList<>();

            //Query the server (Fire Base API) and sort by start date
            final Query eventsByDate = ref.child("calendar").orderByChild("start_date");
            eventsByDate.addChildEventListener(new ChildEventListener() {

                // Retrieve new posts as they are added to Fire base asynchronously
                //We are using an anonymous inner class here for simplicity of OOP
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                    //if the context is old, do not resume
                    if (!getData) {
                        eventsByDate.removeEventListener(this);
                        return;
                    }

                    //Extracting the JSON data from the server as a Map (Key, Value Pairs)
                    //Constructing a custom Event object from the data
                    Map<String, Object> newEvent = (Map<String, Object>) snapshot.getValue();
                    boolean isGoing = snapshot.child("rsvp")
                            .child(DataHolder.getUID()).getValue() != null;
                    Log.v(LOG_TAG, instanceID + "");
                    events.add(new EventItem(
                            snapshot.getKey(),
                            newEvent.get("id").toString()                                                     ,
                            newEvent.get("start_date").toString(),
                            newEvent.get("end_date").toString(),
                            newEvent.get("start_time").toString(),
                            newEvent.get("end_time").toString(),
                            newEvent.get("title").toString(),
                            newEvent.get("location").toString(),
                            newEvent.get("details").toString(),
                            newEvent.get("url").toString(),
                            newEvent.get("contact_info").toString(),
                            newEvent.get("category").toString(),
                            isGoing,
                            parentActivity
                    ));

                    DataStorage.writeToFile(events, parentActivity);

                    Collections.sort(events, eventCompare);
                    extendsToday();

                    mListView.setAdapter(eventAdapter);
                    mImageAdapter = new BannerAdapter(parentActivity, showEvents);
                    mScrollBanner.setAdapter(mImageAdapter);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                //if fire base object removed
                //remove it from the events list
                //& update events list accordingly
                //basically re instantiate it
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //if the context is old, do not resume
                    if (!getData) {
                        eventsByDate.removeEventListener(this);
                        return;
                    }

                    String removedID = dataSnapshot.child("id").getValue().toString();
                    Iterator<EventItem> x  = events.iterator();
                    while(x.hasNext()) {
                        EventItem t = x.next();
                        if (t.getId().equals(removedID))
                            x.remove();
                    }
                    mImageAdapter = new BannerAdapter(parentActivity, showEvents);
                    mScrollBanner.setAdapter(mImageAdapter);
                    mScrollBanner.setOffscreenPageLimit(3);
                }
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(LOG_TAG, firebaseError.getMessage());
                }
            });
        }


        extendsToday();

        //Banner Adapter
        mImageAdapter = new BannerAdapter(getActivity(), showEvents);
        mScrollBanner = (ViewPager) rootView.findViewById(R.id.scrolling_banner);
        mScrollBanner.setAdapter(mImageAdapter);
        mScrollBanner.setOffscreenPageLimit(3);
        //When a page changes on a banner the bar smooth scrolls to position
        mScrollBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                //set the progress bar accordingly
                int bar = (int) (position *
                        (double) mProgressBar.getMax() / (showEvents.size() - 1) + 0.5);
                ObjectAnimator anim = ObjectAnimator.ofInt(mProgressBar, "progress", bar);
                anim.setDuration(350);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }

        });

        eventAdapter = new EventListItemAdapter(getActivity(), showEvents);
        mListView.setAdapter(eventAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent to detail activity with position extra and data
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, showEvents.get(position).getNumber());
                detailIntent.putExtra(TITLE_KEY, showEvents.get(position).getmTitle());
                detailIntent.putExtra(START_DATE_KEY, showEvents.get(position).getmStartDate());
                detailIntent.putExtra(END_DATE_KEY, showEvents.get(position).getmEndDate());
                detailIntent.putExtra(START_TIME_KEY, showEvents.get(position).getmStartTime());
                detailIntent.putExtra(END_TIME_KEY, showEvents.get(position).getmEndTime());
                detailIntent.putExtra(LOCATION_KEY, showEvents.get(position).getmLocation());
                detailIntent.putExtra(DETAILS_KEY, showEvents.get(position).getmDetails());
                detailIntent.putExtra(URL_KEY, showEvents.get(position).getmUrl());
                detailIntent.putExtra(CONTACT_INFO_KEY, showEvents.get(position).getmContactInfo());
                detailIntent.putExtra(CATEGORY_KEY, showEvents.get(position).getCategory());
                detailIntent.putExtra(RSVP_KEY, showEvents.get(position).isAttending());

                startActivity(detailIntent);
                ((HomepageActivity) getActivity()).animToDetail();

            }
        });

        rotatedScreen = false;

        //if was started by notification, then go to it.
        Intent notifIntent = getActivity().getIntent();
        if (notifIntent != null && notifIntent.getBooleanExtra(PushReceiver.PUSH_REDIRECT_KEY, false)) {
            Intent redirectIntent = new Intent(getActivity(), PushActivity.class);
            redirectIntent.putExtra(PushReceiver.PUSH_DETAILS_KEY,
                    notifIntent.getStringExtra(PushReceiver.PUSH_DETAILS_KEY));
            redirectIntent.putExtra(PushReceiver.PUSH_MSG_KEY,
                    notifIntent.getStringExtra(PushReceiver.PUSH_MSG_KEY));
            startActivity(redirectIntent);
        }

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onDestroy() {
        super.onDestroy();
        if (!parentActivity.isFinishing()) {
                //screen rotation
            rotatedScreen = true;
        }
        getData = false;
    }

    private void initDrawer(Activity context) {
        DrawerAdapter mDrawerAdapter = new DrawerAdapter(context, DataHolder.getDrawerArray());
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setBackgroundResource(R.color.drawer_background);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
               context, mDrawerLayout, mDrawerList));

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public void onResume(){
        super.onResume();
        activity = getActivity();
    }

    public static void extendsToday() {
        showEvents.clear();
        String todayDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(Calendar.getInstance().getTime());
        for (EventItem thisEvent : events) {
            if (thisEvent.getmStartDate().compareTo(todayDate) > -1) {
                showEvents.add(thisEvent);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerToggle.syncState();
        activity = getActivity();

    }

    public static ArrayList<EventItem> getEvents() {
        return events;
    }
    public static int getEventsPositionForNumQuery(String numQuery) {

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getNumber().equals(numQuery))
                return i;
        }

        return -1;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage, secondary;
        Bitmap imageBit;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public void updateImageView(ImageView newImgView) {
            bmImage = newImgView;
            if (imageBit != null)
                bmImage.setImageBitmap(imageBit);
        }

        protected Bitmap doInBackground(String... urls) {

            //only do this if they dont already have the image

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                if (isCancelled())
                    return null;
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Bitmap", e.getMessage() + " " + e.getLocalizedMessage() + " error");
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imageBit = result;
            bmImage.setImageBitmap(result);
            if (secondary != null) {
                secondary.setImageBitmap(result);
            }
        }

        public void setSecondary(ImageView detailView) {
            secondary = detailView;
        }

        public Bitmap getBitmap() {
            return imageBit;
        }

    }
}

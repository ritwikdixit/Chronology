package com.ritwik.android.madfbla201415;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
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
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.activeandroid.query.Select;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.ritwik.android.madfbla201415.Database.DataModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomepageFragment extends Fragment {

    //the scrolling image banner with a ViewPager, and adapter
    // essentially a list of images being animated by runnable

    private ViewPager mScrollBanner;
    private ListView mListView;
    private PagerAdapter mImageAdapter;
    private ProgressBar mProgressBar;
    private LinearLayout mScrollerLayout;

    private EventListItemAdapter eventAdapter;

    private int scrollY = -1;
    private boolean scrolled = false;
    private int beforeFirst = 0;
    private int translationY = 0;

    //this is static so i can refer to it in the other class
    //detail activity
    private static ArrayList<EventItem> events;

    private static final String LOG_TAG = "EventList";
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

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private SearchView mSearch;

    private Comparator eventCompare;

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

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });
        toolbar.setTitle("Welcome!");

        //init the drawer
        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) rootView.findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.drawer_list_item, R.id.list_item_text, DataHolder.getDrawerArray()));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
                getActivity(), mDrawerLayout, mDrawerList));

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout,  toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //set all items unchecked
                for (int i = 0; i < DataHolder.getDrawerArray().length; i++) {
                    mDrawerList.setItemChecked(i, false);
                }
                getActivity().supportInvalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setBackgroundResource(R.color.drawer_background);

        //for aesthetics
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        //On Creation of Homepage, store user Data
        if(DataHolder.hasUserData())
            ref.child("users").child(DataHolder.getUID())
                    .addChildEventListener(new ChildEventListener() {

                // Retrieve new posts as they are added to Firebase
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                    Map<String, Object> newUser = (Map<String, Object>) snapshot.getValue();
                    DataHolder.setEmail(newUser.get("email").toString());
                    DataHolder.setName(newUser.get("full_name").toString());
                    DataHolder.setPhoneNumber(newUser.get("phone_number").toString());
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("Loading User Data Error", firebaseError.getMessage());
                }
            });

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mScrollerLayout = (LinearLayout) rootView.findViewById(R.id.scroller_layout);

        if (events == null) {

            eventCompare = new Comparator<EventItem>() {

                @Override
                public int compare(EventItem event, EventItem event2) {
                    return event.getmStartDate().compareTo(event2.getmStartDate());
                }
            };

            events = new ArrayList<>();
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
                    String id = newEvent.get("id").toString();
                    List<DataModel> ldm = new Select().from(DataModel.class).where("myID = ?", id).execute();
                    if (ldm.size() == 0) {
                        DataModel dm = new DataModel(newEvent.get("start_date").toString(),
                                newEvent.get("end_date").toString(),
                                newEvent.get("start_time").toString(),
                                newEvent.get("end_time").toString(),
                                newEvent.get("title").toString(),
                                newEvent.get("location").toString(),
                                newEvent.get("details").toString(),
                                newEvent.get("url").toString(),
                                newEvent.get("contact_info").toString(),
                                id);
                        dm.save();
                    }

                    Collections.sort(events, eventCompare);

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
        }

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

                startActivity(detailIntent);
                ((HomepageActivity) getActivity()).animToDetail();

            }
        });

        mListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScrollChanged();
                ViewTreeObserver observe = mListView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    observe.removeOnGlobalLayoutListener(this);
                 else
                    observe.removeGlobalOnLayoutListener(this);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onScrollChanged();
            }
        });

        return rootView;
    }

    private void onScrollChanged() {

        View itemOne = mListView.getChildAt(0);

        int top;
        if (itemOne == null)
            top = 0;
        else
            top = itemOne.getTop();

        if (mListView.getFirstVisiblePosition() == 0) {
            //mProgressBar.setTranslationY(Math.max(0, mScrollBanner.getTop() + top));
            mScrollerLayout.setTranslationY(top);
            mListView.setVerticalScrollBarEnabled(false);

        }


    }

    public static void initEvents(final Activity context) {

        Firebase ref = DataHolder.getRef();
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
                        context
                ));
                String id = newEvent.get("id").toString();
                List<DataModel> ldm = new Select().from(DataModel.class).where("myID = ?", id).execute();
                if(ldm.size() == 0){
                    DataModel dm = new DataModel( newEvent.get("start_date").toString(),
                            newEvent.get("end_date").toString(),
                            newEvent.get("start_time").toString(),
                            newEvent.get("end_time").toString(),
                            newEvent.get("title").toString(),
                            newEvent.get("location").toString(),
                            newEvent.get("details").toString(),
                            newEvent.get("url").toString(),
                            newEvent.get("contact_info").toString(),
                            id);
                    dm.save();
                }

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

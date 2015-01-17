package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Joshua actually.
 */

//for admins to create events and send them to the server
//quickly and efficiently, the data will be updated immediately
public class AdminPanelActivity extends ActionBarActivity  {

    private Activity mContext = this;

    private TextView mDate, mTime;
    private EditText mTitle, mLocation, mContact, mImageUrl, mDetails;
    private Button mCreateEventButton, mStartButtonDate,
            mEndButtonDate, mStartButtonTime, mEndButtonTime;

    private String startDate, endDate, startTime, endTime;
    private Firebase ref;
    private boolean pressed = false, started = false;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar toolbar;
    private SearchView mSearch;

    private static final String STANDARD_IMAGE_URL = "http://hhsprogramming.com/img/logo-white.png";
    public static final String LOG_TAG = "AdminPanel";
    public static final String START_KEY = "start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Firebase.setAndroidContext(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        toolbar.inflateMenu(R.menu.menu_main);

        //init the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, R.id.list_item_text, DataHolder.getDrawerArray()));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
                this, mDrawerLayout, mDrawerList));

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {

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

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerList.setBackgroundResource(R.color.drawer_background);

        //actual unique onCreate starts here

        mTitle = (EditText) findViewById(R.id.admin_title_edit);
        mDate = (TextView)findViewById(R.id.admin_date_text);
        mTime = (TextView)findViewById(R.id.admin_time_text);
        mLocation = (EditText) findViewById(R.id.admin_location_edit);
        mContact = (EditText) findViewById(R.id.admin_contact_edit);
        mImageUrl = (EditText) findViewById(R.id.admin_image_url);
        mDetails = (EditText) findViewById(R.id.admin_details_edit);
        mCreateEventButton = (Button) findViewById(R.id.admin_form_submit);

        mStartButtonTime = (Button) findViewById(R.id.admin_start_time_button);
        mEndButtonTime = (Button) findViewById(R.id.admin_end_time_button);
        mStartButtonDate = (Button) findViewById(R.id.admin_start_date_button);
        mEndButtonDate = (Button) findViewById(R.id.admin_end_date_button);

        setDialogListeners();

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pressed) {
                    pressed = true;
                    if (ref == null)
                        ref = DataHolder.getRef();
                    try {
                        uploadEvent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void setDialogListeners() {

        mStartButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean(START_KEY, true);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "str");

            }
        });

        mEndButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean(START_KEY, false);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "str");

            }
        });

        mStartButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean(START_KEY, true);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "str");

            }
        });

        mEndButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean(START_KEY, false);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "str");

            }
        });
    }

    private void uploadEvent() throws IOException {

        //get the current text fields data, and put it in the map. Then upload

        String eventName = "event" + (HomepageFragment.getEvents().size() + 1);

        //removes all punctuation from title, to lower case, and adds underscores
        String id = mTitle.getText().toString()
                .replaceAll("[^a-zA-Z ]", "").replaceAll(" ", "_").toLowerCase();

        Map<String, Object> eventData = new HashMap<>();

        if(id == null) id = UUID.randomUUID().toString();;
        eventData.put(HomepageFragment.ID_KEY, id);
        String tempTitle = "";
        if(mTitle.getText().toString() != null) tempTitle = mTitle.getText().toString();
        eventData.put(HomepageFragment.TITLE_KEY, tempTitle);
        if(startDate == null) startDate = "0000-00-00";
        eventData.put(HomepageFragment.START_DATE_KEY, startDate);
        if(endDate == null) endDate = "0000-00-00";
        eventData.put(HomepageFragment.END_DATE_KEY, endDate);
        if(startTime == null) startTime = "12:00AM";
        eventData.put(HomepageFragment.START_TIME_KEY, startTime);
        if(endTime == null) endTime = "12:00AM";
        eventData.put(HomepageFragment.END_TIME_KEY, endTime);
        String tempLoc = "";
        if(mLocation.getText().toString() != null) tempLoc = mLocation.getText().toString();
        eventData.put(HomepageFragment.LOCATION_KEY, tempLoc);
        String tempContact = "";
        if(mContact.getText().toString() != null) tempContact = mContact.getText().toString();
        eventData.put(HomepageFragment.CONTACT_INFO_KEY, tempContact);

        //optional image puts standard if text is blank
        if (mImageUrl.getText().toString().replaceAll("\\s", "").equals(""))
            eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        else if(URLUtil.isValidUrl(mImageUrl.getText().toString())){
            URLConnection connection = new URL(mImageUrl.getText().toString()).openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            boolean image = contentType.startsWith("image/");
            if(image)
                eventData.put(HomepageFragment.URL_KEY, mImageUrl.getText().toString());
            else
                eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        }
        else {
            eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        }


        eventData.put(HomepageFragment.DETAILS_KEY, mDetails.getText().toString());

        Log.v(LOG_TAG, "event = " + eventName);
        Log.v(LOG_TAG, ">----data is " + Arrays.toString(eventData.entrySet().toArray()));

        ref.child("calendar").child(eventName).updateChildren(eventData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                pressed = false;
                new AlertDialog.Builder(mContext)
                        .setTitle("Event Created")
                        .setMessage("Congratulations! You've successfully created an event.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //android library API 11+ standard search
        SearchManager managerSearch = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.chronology_search_bar).getActionView();
        mSearch.setSearchableInfo(managerSearch.getSearchableInfo(getComponentName()));
        mSearch.setIconifiedByDefault(true);

        return true;
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
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //in addition to setting start date, set button text

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        mStartButtonDate.setText(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        mEndButtonDate.setText(endDate);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        mStartButtonTime.setText(startTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        mEndButtonTime.setText(endTime);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        boolean isStart;

        //restores what was previously set
        static int[] prevData = {-1, -1};

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            final Calendar c = Calendar.getInstance();
            int hour, minute;

            //if no previous data, load current data, otherwise load saved data
            if (prevData[0] == -1) {
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = prevData[0];
                minute = prevData[1];
            }

            isStart = this.getArguments().getBoolean(START_KEY);
            Log.v(LOG_TAG, "boolean " + isStart);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            //set data
            prevData[0] = hourOfDay;
            prevData[1] = minute;

            //military time convert
            String ampm = "AM";
            String minuteStr;

            if (hourOfDay >= 12) {
                ampm = "PM";
                if (hourOfDay > 12)
                    hourOfDay -= 12;
            }

            if(hourOfDay == 0)
                hourOfDay = 12;

            if (minute <= 10)
                minuteStr = "0" + minute;
            else
                minuteStr = "" + minute;

            String timeFormat = hourOfDay + ":" + minuteStr + ampm;
            Log.v(LOG_TAG, timeFormat);

            if (isStart)
                ((AdminPanelActivity)getActivity()).setStartTime(timeFormat);
            else
                ((AdminPanelActivity)getActivity()).setEndTime(timeFormat);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        boolean isStart;
        static int[] prevData = {-1, -1, -1};

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year, month, day;

            if (prevData[0] == -1) {

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

            } else {

                year = prevData[0];
                month = prevData[1];
                day = prevData[2];

            }

            isStart = this.getArguments().getBoolean(START_KEY);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            //save the data
            prevData[0] = year;
            prevData[1] = month;
            prevData[2] = day;

            String dayStr, monthStr;
            month++;

            if (month < 10) {
                monthStr = "0" + month;
            } else {
                monthStr = "" + month;
            }

            if (day < 10) {
                dayStr = "0" + day;
            } else {
                dayStr = "" + day;
            }

            String dateFormat = year + "-" + monthStr + "-" + dayStr;
            Log.v(LOG_TAG, dateFormat);

            if (isStart)
                ((AdminPanelActivity)getActivity()).setStartDate(dateFormat);
            else
                ((AdminPanelActivity)getActivity()).setEndDate(dateFormat);
        }
    }

}

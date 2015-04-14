package com.ritwik.madfbla201415;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.ritwik.android.madfbla201415.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joshua actually.
 */

//for admins to create events and send them to the server
//quickly and efficiently, the data will be updated immediately
public class AdminPanelActivity extends ActionBarActivity  {

    private Activity mContext = this;

    private EditText mTitle, mLocation, mContact, mImageUrl, mDetails;
    private Button mCreateEventButton, mStartButtonDate,
            mEndButtonDate, mStartButtonTime, mEndButtonTime;
    private RadioButton mSports, mClubs, mHoliday, mAcademics, mFun;

    private String startDate, endDate, startTime, endTime;
    private Firebase ref;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar toolbar;
    private SearchView mSearch;


    //part of the checking system to see if valid image url
    private static final String STANDARD_IMAGE_URL = "http://hhsprogramming.com/img/logo-white.png";
    private static final String[] EXTENSIONS = {".jpg", ".png", ".tif", ".tiff", ".gif"};

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

        DrawerAdapter mDrawerAdapter = new DrawerAdapter(this, DataHolder.getDrawerArray());
        mDrawerList.setAdapter(mDrawerAdapter);

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
        mLocation = (EditText) findViewById(R.id.admin_location_edit);
        mContact = (EditText) findViewById(R.id.admin_contact_edit);
        mImageUrl = (EditText) findViewById(R.id.admin_image_url);
        mDetails = (EditText) findViewById(R.id.admin_details_edit);
        mCreateEventButton = (Button) findViewById(R.id.admin_form_submit);

        mStartButtonTime = (Button) findViewById(R.id.admin_start_time_button);
        mEndButtonTime = (Button) findViewById(R.id.admin_end_time_button);
        mStartButtonDate = (Button) findViewById(R.id.admin_start_date_button);
        mEndButtonDate = (Button) findViewById(R.id.admin_end_date_button);

        mSports = (RadioButton) findViewById(R.id.admin_radio_sports);
        mClubs = (RadioButton) findViewById(R.id.admin_radio_clubs);
        mHoliday = (RadioButton) findViewById(R.id.admin_radio_holiday);
        mFun = (RadioButton) findViewById(R.id.admin_radio_fun);
        mAcademics = (RadioButton) findViewById(R.id.admin_radio_academics);

        mSports.setChecked(true);

        setDialogListeners();

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ref == null)
                    ref = DataHolder.getRef();

                //make sure they filled all fields
                if (!fieldsAreNull()) {
                    uploadEvent();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Fields must not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void clearFields() {

        mTitle.setText("New Event Title");
        mContact.setText("");
        mLocation.setText("");
        mImageUrl.setText("");
        mDetails.setText("");

        mStartButtonDate.setText("Select Start Date");
        mEndButtonDate.setText("Select End Date");

        mEndButtonTime.setText("Select End Time");
        mStartButtonTime.setText("Select Start Time");

    }

    private boolean fieldsAreNull() {

        //checks if any necessary field is null
        //if any fields are null reject (return false) don't allow event create
        return mTitle.getText().toString().replaceAll("\\s", "").equals("")
                || mStartButtonDate.getText().toString().equals("Select Start Date")
                || mEndButtonDate.getText().toString().equals("Select End Date")
                || mStartButtonTime.getText().toString().equals("Select Start Time")
                || mEndButtonTime.getText().toString().equals("Select End Time")
                || mLocation.getText().toString().replaceAll("\\s", "").equals("")
                || mDetails.getText().toString().replaceAll("\\s", "").equals("")
                || mContact.getText().toString().replaceAll("\\s", "").equals("");

    }

    private void setDialogListeners() {

        //for the corresponding buttons, pop a fragment showing the chooser

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

    //checks if valid image
    private boolean validURL(String URLStr) {

        if (URLUtil.isValidUrl(URLStr)) {
            for (String s : EXTENSIONS)
                if (URLStr.endsWith(s))
                    return true;
        }

        return false;
    }

    private void uploadEvent() {

        //get the current text fields data, and put it in the map. Then upload

        String eventName = "event" + (HomepageFragment.getEvents().size() + 1);

        //removes all punctuation from title, to lower case, and adds underscores
        String id = mTitle.getText().toString()
                .replaceAll("[^a-zA-Z ]", "").replaceAll(" ", "_").toLowerCase();

        Map<String, Object> eventData = new HashMap<>();

        eventData.put(HomepageFragment.ID_KEY, id);
        eventData.put(HomepageFragment.TITLE_KEY, mTitle.getText().toString());
        eventData.put(HomepageFragment.START_DATE_KEY, startDate);
        eventData.put(HomepageFragment.END_DATE_KEY, endDate);
        eventData.put(HomepageFragment.START_TIME_KEY, startTime);
        eventData.put(HomepageFragment.END_TIME_KEY, endTime);
        eventData.put(HomepageFragment.LOCATION_KEY, mLocation.getText().toString());
        eventData.put(HomepageFragment.CONTACT_INFO_KEY, mContact.getText().toString());
        eventData.put(HomepageFragment.CATEGORY_KEY, getTheCategory());
        eventData.put(HomepageFragment.RSVP_KEY, "");

        //optional image puts standard if text is blank
        if (mImageUrl.getText().toString().replaceAll("\\s", "").equals(""))
            eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        else if (validURL(mImageUrl.getText().toString().replaceAll("\\s", "")))
            eventData.put(HomepageFragment.URL_KEY, mImageUrl.getText().toString());
        else {
            Toast.makeText(getApplicationContext(),
                    "Invalid Image URL, used standard image", Toast.LENGTH_SHORT).show();
            eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        }

        eventData.put(HomepageFragment.DETAILS_KEY, mDetails.getText().toString());

        //init rsvp
        Map rsvpInit = new HashMap<String, Object>();
        rsvpInit.put("count", "0");
        eventData.put(HomepageFragment.RSVP_KEY, rsvpInit);

        ref.child("calendar").child(eventName)
                .updateChildren(eventData, new Firebase.CompletionListener() {

            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Event Created")
                        .setMessage("Congratulations! You've successfully created an event.")
                        .setPositiveButton("More Events", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                clearFields();
                            }
                        })
                        .setNegativeButton("Back Home", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    public String getTheCategory() {

        if (mClubs.isChecked()) {
            return HomepageFragment.CAT_CLUB_KEY;
        } else if (mAcademics.isChecked()) {
            return HomepageFragment.CAT_ACADEMICS_KEY;
        } else if (mSports.isChecked()) {
            return HomepageFragment.CAT_SPORTS_KEY;
        } else if (mFun.isChecked()) {
            return HomepageFragment.CAT_FUN_KEY;
        } else if (mHoliday.isChecked()) {
            return HomepageFragment.CAT_HOLIDAY_KEY;
        }

        //this should never happen
        return "error ?maybe";

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

        // Handles action bar item clicks here.

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
        //this is for convenience, saves data
        //-1 is code for nothing saved, sets current date
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

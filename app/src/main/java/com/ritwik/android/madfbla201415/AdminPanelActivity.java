package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
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

    private TextView mDate, mTime;
    private EditText mTitle, mLocation, mContact, mImage, mDetails;
    private Button mCreateEventButton;

    private String startDate, endDate, startTime, endTime;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar toolbar;
    private SearchView mSearch;

    private static final String STANDARD_IMAGE_URL = "http://hhsprogramming.com/img/logo-white.png";
    public static final String LOG_TAG = "AdminPanel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
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
        mImage = (EditText) findViewById(R.id.admin_image_url);
        mDetails = (EditText) findViewById(R.id.admin_details_edit);
        mCreateEventButton = (Button) findViewById(R.id.admin_form_submit);

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEvent();
            }
        });


    }

    private void uploadEvent() {

        //get the current text fields data, and put it in the map. Then upload
        Map<String, Object> eventData = new HashMap<>();

        eventData.put(HomepageFragment.TITLE_KEY, mTitle.getText().toString());
        eventData.put(HomepageFragment.LOCATION_KEY, mLocation.getText().toString());
        eventData.put(HomepageFragment.CONTACT_INFO_KEY, mLocation.getText().toString());

        //optional image puts standard otherwise
        if (mImage.getText().toString() == null)
            eventData.put(HomepageFragment.URL_KEY, STANDARD_IMAGE_URL);
        else
            eventData.put(HomepageFragment.URL_KEY, mImage.getText().toString());

        eventData.put(HomepageFragment.DETAILS_KEY, mDetails.getText().toString());

        //TODO: finish this, and get the data from the dialogs

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

        return super.onOptionsItemSelected(item);
    }

    // we represent the lollipop guild
    public void showTimePickerDialog(View v) {

        String TAG = "atm i am null";
        if (v.getId() == R.id.admin_start_time_button)
            TAG = "start";
        else if (v.getId() == R.id.admin_end_time_button)
            TAG = "end";

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), TAG);
    }

    // the lollipop guild, the lollipop guild
    public void showDatePickerDialog(View v) {

        String TAG = "atm i am null";
        if (v.getId() == R.id.admin_start_date_button)
            TAG = "start";
        else if (v.getId() == R.id.admin_end_date_button)
            TAG = "end";

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), TAG);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        boolean isStart;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            Log.d(LOG_TAG, "tag = " + getTag());

            if (getTag().equals("start"))
                isStart = true;
            else
                isStart = false;

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        boolean isStart;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            Log.d(LOG_TAG, "tag = " + getTag());

            if (getTag().equals("start"))
                isStart = true;
            else
                isStart = false;
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            if (isStart) {

            } else {

            }

        }
    }

}

package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * Created by Ritwik on 1/9/15.
 */

//don't worry about the methods being deprecated
//they work standard on most devices.

public class SettingsActivity extends PreferenceActivity {

    public static final String SQL_LOG_TAG = "ContentUris";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        LinearLayout root = (LinearLayout)findViewById(
                android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.action_bar, root, false);
        root.addView(toolbar, 0);
        toolbar.setTitle("Settings");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });

    }
}

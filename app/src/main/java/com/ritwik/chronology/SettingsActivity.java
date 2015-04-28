package com.ritwik.chronology;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

//don't worry about the methods being deprecated
//they work better on old devices.

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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(36, 0, 0, 36);
        TextView name = new TextView(this);
        name.setText("Logged in as " + DataHolder.getName());
        name.setTextColor(getResources().getColor(android.R.color.black));
        root.addView(name, layoutParams);

    }
}

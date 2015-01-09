package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Ritwik on 1/9/15.
 */

//don't worry about the methods being deprecated
//they work standard on most devices.

public class SettingsActivity extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}

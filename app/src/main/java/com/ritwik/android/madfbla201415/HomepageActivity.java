package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HomepageActivity extends ActionBarActivity {

    private Toolbar toolbar;

    public HomepageActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is temporary so later this will change
        setContentView(R.layout.activity_homepage);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.homepage_fragment_container, new HomepageFragment())
                    .commit();
        }

    }


}

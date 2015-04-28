package com.ritwik.chronology;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HomepageActivity extends ActionBarActivity {

    public HomepageActivity() {

    }

    public void animToDetail() {
        HomepageActivity.this
                .overridePendingTransition(R.anim.right_to_left, R.anim.neg_right_left);
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

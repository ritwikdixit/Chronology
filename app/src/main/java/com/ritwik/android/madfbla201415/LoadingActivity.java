package com.ritwik.android.madfbla201415;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

//this class will redirect to the appropriate page
public class LoadingActivity extends ActionBarActivity {

    private Firebase ref;
    private Context mContext;
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private static final String LOG_TAG =  "Loading";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Firebase.setAndroidContext(this);
        DataHolder.setRef(new Firebase(URL_FIREBASE));
        ref = DataHolder.getRef();
        mContext = this;

        //Checks if authorized, starts intent to appropriate activity
        ref.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(AuthData authData) {

                if (authData != null) {
                    Intent redirectIntent = new Intent(mContext, HomepageActivity.class);
                    startActivity(redirectIntent);
                } else {
                    Intent redirectIntent = new Intent(mContext, LoginActivity.class);
                    startActivity(redirectIntent);
                }

                finish();
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        /*
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.v(LOG_TAG, "Error on Sleep");
        }
        */
        ref.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(AuthData authData) {

                if (authData != null) {
                    Intent redirectIntent = new Intent(mContext, HomepageActivity.class);
                    startActivity(redirectIntent);
                } else {
                    Intent redirectIntent = new Intent(mContext, LoginActivity.class);
                    startActivity(redirectIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Maybe have a different action bar menu file later
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

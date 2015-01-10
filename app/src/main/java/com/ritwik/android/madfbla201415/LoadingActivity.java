package com.ritwik.android.madfbla201415;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

//this class will redirect to the appropriate page
public class LoadingActivity extends ActionBarActivity {

    private Firebase ref;
    private Context mContext;
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private static final String LOG_TAG =  "Loading";
    private static boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Firebase.setAndroidContext(this);
        if(DataHolder.isNull())
            DataHolder.setRef(new Firebase(URL_FIREBASE));
        ref = DataHolder.getRef();
        mContext = this;

        //Checks if authorized, starts intent to appropriate activity
        ref.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    DataHolder.setUID(authData.getUid());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Maybe have a different action bar menu file later
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onStart(){
        super.onStart();
        active = true;
    }
    public void onStop(){
        super.onStop();
        active = false;
    }
    public static boolean isActive(){
        return active;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}

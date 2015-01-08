package com.ritwik.android.madfbla201415;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;

/**
 * Created by Ritwik on 1/1/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private Activity mContext;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mPosition;

    private Firebase ref = DataHolder.getRef();
    private static final String LOG_TAG = "Drawer";

    public DrawerItemClickListener(Activity a,
                                   DrawerLayout layout, ListView list) {
        mContext = a;
        mDrawerLayout = layout;
        mDrawerList = list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectItem(position);
    }

    public void selectItem(int position) {

        mPosition = position;
        mDrawerList.setItemChecked(mPosition, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        if (position == 0) {
            //month view
            if(!(mContext instanceof  CalendarActivity)){
                Intent intent = new Intent(mContext, CalendarActivity.class);
                mContext.startActivity(intent);
            }

        } else if (position == 1) {
            //all events
            if(!(mContext instanceof  AllEventsActivity)){
                Intent intent = new Intent(mContext, AllEventsActivity.class);
                mContext.startActivity(intent);
            }

        } else if (position == 2) {
            //home
            if(!(mContext instanceof  HomepageActivity)) {
                Intent intent = new Intent(mContext, HomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }

        } else if (position == 3) {
            //help

        } else if (position == 4) {
            //log out
            ref.unauth();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
        }

    }


}

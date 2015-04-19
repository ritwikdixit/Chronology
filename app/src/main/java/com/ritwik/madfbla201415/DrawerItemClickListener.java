package com.ritwik.madfbla201415;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.ritwik.madfbla201415.Push.AllPushActivity;

public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private Activity mContext;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mPosition;

    //for log out
    private DialogInterface.OnClickListener dialogListener;

    private Firebase ref = DataHolder.getRef();
    private static final String LOG_TAG = "Drawer";

    public DrawerItemClickListener(Activity a,
                                   DrawerLayout layout, ListView list) {
        mContext = a;
        mDrawerLayout = layout;
        mDrawerList = list;
    }

    private void initLogOutDialog() {

        //if the user clicks log out make sure it was not mis click and
        //they actually wanted to log out

        dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == DialogInterface.BUTTON_POSITIVE) {

                    dialog.dismiss();
                    ref.unauth();
                    DataHolder.resetData();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);

                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                }

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener)
                .show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectItem(position);
    }

    public void selectItem(int position) {

        mPosition = position;
        mDrawerList.setItemChecked(mPosition, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        if(DataHolder.isAdmin())
            adminPosition(position);
        else
            userPosition(position);
    }

    private void userPosition(int position) {
        boolean clickedSamePage = false;
        if (position == 0 && !(mContext instanceof HomepageActivity)) {
                Intent intent = new Intent(mContext, HomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);

        } else if (position == 1 && !(mContext instanceof ImprovedCalendarActivity)){
                Intent intent = new Intent(mContext, ImprovedCalendarActivity.class);
                mContext.startActivity(intent);

        } else if (position == 2 && !(mContext instanceof AllEventsActivity)){
                Intent intent = new Intent(mContext, AllEventsActivity.class);
                mContext.startActivity(intent);

        } else if (position == 3 && !(mContext instanceof HelpActivity)) {
                Intent intent = new Intent(mContext, HelpActivity.class);
                mContext.startActivity(intent);

        } else if (position == 4 && !(mContext instanceof AllPushActivity)){
                Intent intent = new Intent(mContext, AllPushActivity.class);
                mContext.startActivity(intent);

        } else if (position == 5) {
            //log out
            initLogOutDialog();
        } else {
            clickedSamePage = true;
        }
        if(!(mContext instanceof HomepageActivity) && !clickedSamePage)
            mContext.finish();
    }

    private void adminPosition(int position) {
        boolean clickedSamePage = false;
        if (position == 0 && !(mContext instanceof HomepageActivity)) {
                Intent intent = new Intent(mContext, HomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);

        } else if (position == 1 && !(mContext instanceof ImprovedCalendarActivity)){
                Intent intent = new Intent(mContext, ImprovedCalendarActivity.class);
                mContext.startActivity(intent);

        } else if (position == 2 && !(mContext instanceof AllEventsActivity)){
                Intent intent = new Intent(mContext, AllEventsActivity.class);
                mContext.startActivity(intent);

        } else if (position == 3 && !(mContext instanceof HelpActivity)) {
                Intent intent = new Intent(mContext, HelpActivity.class);
                mContext.startActivity(intent);

        } else if (position == 4 && !(mContext instanceof AllPushActivity)){
                Intent intent = new Intent(mContext, AllPushActivity.class);
                mContext.startActivity(intent);

        } else if (position == 5) {
            // admin panel create event
            Intent intent = new Intent(mContext, AdminPanelActivity.class);
            mContext.startActivity(intent);

        } else if (position == 6) {
            //log out
            initLogOutDialog();
        } else {
            clickedSamePage = true;
        }

        if(!(mContext instanceof HomepageActivity) && !clickedSamePage)
            mContext.finish();
    }


}

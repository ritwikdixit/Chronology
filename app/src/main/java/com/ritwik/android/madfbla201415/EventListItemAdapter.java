package com.ritwik.android.madfbla201415;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListItemAdapter extends ArrayAdapter<EventItem> {

    private Activity mActivity;

    public EventListItemAdapter(Activity activity, ArrayList<EventItem> events) {
        super(activity, 0, events);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater()
                    .inflate(R.layout.list_item_event, null);
        }

        EventItem thisEvent = getItem(position);

        TextView mDate =  (TextView) convertView.findViewById(R.id.event_list_item_date);
        TextView mTime = (TextView) convertView.findViewById(R.id.event_list_item_time);
        TextView mTitle = (TextView) convertView.findViewById(R.id.event_list_item_title);
        TextView mDetails = (TextView) convertView.findViewById(R.id.event_list_item_details);
        ImageView mCatIcon = (ImageView) convertView.findViewById(R.id.category_icon_container);

        mDate.setText(thisEvent.formatDateCondense(thisEvent.getmStartDate()));
        mTime.setText(thisEvent.getmStartTime());

        String category = thisEvent.getCategory();

        if (category.equals(HomepageFragment.CAT_SPORTS_KEY)) {
            mCatIcon.setImageResource(R.drawable.cat_ic_sports1);

        } else if (category.equals(HomepageFragment.CAT_ACADEMICS_KEY)) {
            mCatIcon.setImageResource(R.drawable.cat_ic_academics);

        } else if (category.equals(HomepageFragment.CAT_CLUB_KEY)) {
            mCatIcon.setImageResource(R.drawable.cat_ic_clubs);

        } else if (category.equals(HomepageFragment.CAT_FUN_KEY)) {
            mCatIcon.setImageResource(R.drawable.cat_ic_fun_event);

        } else if (category.equals(HomepageFragment.CAT_HOLIDAY_KEY)) {
            mCatIcon.setImageResource(R.drawable.cat_ic_holiday);
        }


        if (thisEvent.getmTitle().length() <= 22) {
            mTitle.setText(thisEvent.getmTitle());
        } else {
            mTitle.setText(thisEvent.getmTitle().substring(0, 20) + "...");
        }

        //populating a preview of the detail0
        if (thisEvent.getmDetails().length() <= 27) {
            mDetails.setText(thisEvent.getmDetails());
        } else {
            mDetails.setText(thisEvent.getmDetails().substring(0, 25) + "...");
        }

        return convertView;

    }
}

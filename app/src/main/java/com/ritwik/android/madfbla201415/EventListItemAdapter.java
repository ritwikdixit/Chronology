package com.ritwik.android.madfbla201415;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        mDate.setText(thisEvent.formatDate(thisEvent.getmStartDate()));
        mTime.setText(thisEvent.getmStartTime());
        mTitle.setText(thisEvent.getmTitle());

        //populating a preview of the detail0
        if (thisEvent.getmDetails().length() <= 30) {
            mDetails.setText(thisEvent.getmDetails());
        } else {
            mDetails.setText(thisEvent.getmDetails().substring(0, 30) + "...");
        }

        return convertView;

    }
}

package com.ritwik.madfbla201415.Push;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ritwik.madfbla201415.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PushListItemAdapter extends ArrayAdapter<PushItem> {

    private Activity mActivity;
    private ArrayList<PushItem> pushes;

    public PushListItemAdapter(Activity activity, ArrayList<PushItem> pushes) {
        super(activity, 0, pushes);
        mActivity = activity;
        this.pushes = new ArrayList<>(pushes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater()
                    .inflate(R.layout.list_item_push, null);
        }

        PushItem thisPush = getItem(position);

        TextView mTitle = (TextView) convertView.findViewById(R.id.event_list_item_title);
        TextView mDetails = (TextView) convertView.findViewById(R.id.event_list_item_details);

        String message = thisPush.getMessage();
        String details = thisPush.getDetails();

        //if the message is too long, do not populate full list view
        if (message.length() <= 45){
            mTitle.setText(message);
        } else {
            mTitle.setText(message.substring(0, 42) + "...");
        }

        if (details.length() <= 45){
            mDetails.setText(details);
        } else {
            mDetails.setText(details.substring(0, 45) + "...");
        }

        return convertView;

    }
}

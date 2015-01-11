package com.ritwik.android.madfbla201415;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ritwik.android.madfbla201415.Database.PushModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PushListItemAdapter extends ArrayAdapter<PushItem> {

    private Activity mActivity;

    public PushListItemAdapter(Activity activity, ArrayList<PushItem> events) {
        super(activity, 0, events);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater()
                    .inflate(R.layout.list_item_push, null);
        }

        PushItem thisPush = new PushItem(getRecord(position));

        TextView mDate =  (TextView) convertView.findViewById(R.id.event_list_item_date);
        TextView mTime = (TextView) convertView.findViewById(R.id.event_list_item_time);
        TextView mTitle = (TextView) convertView.findViewById(R.id.event_list_item_title);
        TextView mDetails = (TextView) convertView.findViewById(R.id.event_list_item_details);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat hdf = new SimpleDateFormat("hh:mm a");
        Date resultdate = new Date(thisPush.getTime());

        mDate.setText(sdf.format(resultdate));
        mTime.setText(hdf.format(resultdate));
        mTitle.setText(thisPush.getMessage());
        mDetails.setText(thisPush.getDetails());

        //populating a preview of the detail0
        if (thisPush.getDetails().length() <= 30) {
            mDetails.setText(thisPush.getDetails());
        } else {
            mDetails.setText(thisPush.getDetails().substring(0, 30) + "...");
        }

        return convertView;

    }
    public static PushModel getRecord(int position) {
        List<PushModel> x = new Select().from(PushModel.class).orderBy("Time ASC").execute();
        return x.get(position);
    }
}

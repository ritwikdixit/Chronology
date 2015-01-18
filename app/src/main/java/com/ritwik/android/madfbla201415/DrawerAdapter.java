package com.ritwik.android.madfbla201415;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Ritwik on 1/16/15.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

    private String[] mArray;
    private Context mContext;

    public DrawerAdapter(Context context, String[] array) {
        super(context, 0, Arrays.asList(array));
        mContext = context;
        mArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(mContext).inflate(R.layout.drawer_list_item, null);
        }

        ImageView mIcon = (ImageView) convertView.findViewById(R.id.drawer_icon_container);
        TextView mTitle = (TextView) convertView.findViewById(R.id.list_item_text);

        int resId = DataHolder.getDrawerIdForIndex(position);

        mTitle.setText(mArray[position]);
        mIcon.setImageResource(resId);

        return convertView;

    }
}

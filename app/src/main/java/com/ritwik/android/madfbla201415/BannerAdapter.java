package com.ritwik.android.madfbla201415;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

//Custom Adapter for custom scrolling image banner

public class BannerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<EventItem> mEvents;

    public BannerAdapter(Context context, ArrayList<EventItem> events) {
        mContext = context;
        mEvents = new ArrayList<>(events);
    }

    @Override
    public boolean isViewFromObject(View v, Object object) {
        return v == ((ImageView) object);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView image = mEvents.get(position).getmImage();
        container.addView(image);

        //set listener
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT,
                        mEvents.get(position).getNumber());
                detailIntent.putExtra(HomepageFragment.TITLE_KEY,
                        mEvents.get(position).getmTitle());
                detailIntent.putExtra(HomepageFragment.START_DATE_KEY,
                        mEvents.get(position).getmStartDate());
                detailIntent.putExtra(HomepageFragment.END_DATE_KEY,
                        mEvents.get(position).getmEndDate());
                detailIntent.putExtra(HomepageFragment.START_TIME_KEY,
                        mEvents.get(position).getmStartTime());
                detailIntent.putExtra(HomepageFragment.END_TIME_KEY,
                        mEvents.get(position).getmEndTime());
                detailIntent.putExtra(HomepageFragment.LOCATION_KEY,
                        mEvents.get(position).getmLocation());
                detailIntent.putExtra(HomepageFragment.DETAILS_KEY,
                        mEvents.get(position).getmDetails());
                detailIntent.putExtra(HomepageFragment.URL_KEY,
                        mEvents.get(position).getmUrl());
                detailIntent.putExtra(HomepageFragment.CONTACT_INFO_KEY,
                        mEvents.get(position).getmContactInfo());
                detailIntent.putExtra(HomepageFragment.CATEGORY_KEY,
                        mEvents.get(position).getCategory());
                detailIntent.putExtra(HomepageFragment.RSVP_KEY,
                        mEvents.get(position).isAttending());

                mContext.startActivity(detailIntent);
            }
        });

        return image;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

}

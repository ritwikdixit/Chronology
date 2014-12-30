package com.ritwik.android.madfbla201415;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//Custom Adapter for custom scrolling image banner

public class BannerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mResourceIds;

    public BannerAdapter(Context context, int[] ids) {
        mContext = context;
        mResourceIds = ids.clone();
    }

    @Override
    public boolean isViewFromObject(View v, Object object) {
        return v == ((ImageView) object);
    }

    @Override
    public int getCount() {
        return mResourceIds.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView image = new ImageView(mContext);
        image.setImageResource(mResourceIds[position]);
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}

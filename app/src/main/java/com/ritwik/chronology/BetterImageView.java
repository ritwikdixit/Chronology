package com.ritwik.chronology;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ritwik on 1/4/15.
 */


//class to scale images properly in the detail activity

public class BetterImageView extends ImageView {

    public BetterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final Drawable d = getDrawable();
        if (d != null) {

            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = (int) (Math.ceil(width *
                    (float) d.getIntrinsicHeight() / d.getIntrinsicWidth()));
            this.setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

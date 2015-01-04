package com.ritwik.android.madfbla201415;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Ritwik on 1/2/15.
 */

//listens for a left swipe
public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Log.v("EventList", "Works");
        //left swipe
        if (velocityX < 0) {
            return true;
        }
        return false;
    }
}

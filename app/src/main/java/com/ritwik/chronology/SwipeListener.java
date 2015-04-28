package com.ritwik.chronology;

import android.view.GestureDetector;
import android.view.MotionEvent;


//listens for a left to right swipe
public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    public static final String TAG = "Swipes";
    private boolean swiped = false;


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX > 1500 && Math.abs(velocityY) < 2000) {
            swiped = true;
        }

        return true;
    }

    public boolean hasSwiped() {
        return swiped;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
}

package com.ritwik.android.madfbla201415;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.ritwik.android.madfbla201415.Database.PushModel;

public class PushItem {

    private long recieveTime;
    private String message;
    private String details;

    public PushItem(long recieveTime, String message, String details) {
        this.recieveTime = recieveTime;
        this.message = message;
        this.details = details;
    }
    public PushItem(PushItem x) {
        this.recieveTime = x.recieveTime;
        this.message = x.message;
        this.details = x.details;
    }
    public PushItem(PushModel m) {
        this.recieveTime = m.time;
        this.message = m.name;
        this.details = m.details;
    }

    public long getTime(){ return recieveTime; }
    public String getMessage(){ return message; }
    public String getDetails(){ return details; }

    //this is for debugging do not delete
    @Override
    public String toString() {
        return message + ": " + details;
    }
}

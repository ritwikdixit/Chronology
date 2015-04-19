package com.ritwik.madfbla201415.Push;

import android.util.Log;

public class PushItem {

    private String message;
    private String details;

    public PushItem(String message, String details) {
        this.message = message;
        this.details = details;
    }
    public PushItem(PushItem x) {
        this.message = x.message;
        this.details = x.details;
    }

    public PushItem(String[] values) {
        this(values[0], values[1]);
    }

    public String getMessage(){ return message; }
    public String getDetails(){ return details; }

    //this is for debugging do not delete
    @Override
    public String toString() {
        return message + "::" + details + "&&&";
    }
}

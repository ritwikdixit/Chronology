package com.ritwik.android.madfbla201415;


import com.firebase.client.Firebase;
import java.util.ArrayList;
public class DataHolder {

    private static String LOG_TAG = "EventList";
    private static Firebase ref;
    private static ArrayList<EventItem> events = new ArrayList<>();

    private DataHolder(){} //Empty Constructor


    public static void setRef(Firebase r){
        ref = r;
    }

    public static Firebase getRef(){
        return ref;
    }



}

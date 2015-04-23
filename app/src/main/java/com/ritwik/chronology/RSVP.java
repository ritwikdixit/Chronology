package com.ritwik.chronology;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class RSVP {

    final static String GOING = "true";
    final static String NOT_GOING = "false";
    public static Firebase ref = DataHolder.getRef();

    //Example Use:      RSVP.rsvp("event1", RSVP.GOING);
    public static void rsvp(final String eventID, String state) {
        if (state.equals(GOING)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(DataHolder.getUID(), state);
            ref.child("calendar").child(eventID).child("rsvp").updateChildren(map);
        } else if (state.equals(NOT_GOING)) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    ref.child("calendar")
                            .child(eventID)
                            .child("rsvp").child(DataHolder.getUID().toString()).removeValue();
                }
            });
            t.start();
        }
    }

    public static boolean isGoing(int position) {
        return HomepageFragment.getEvents().get(position).isAttending();
    }

}
package com.ritwik.android.madfbla201415;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RSVP {
        final static String GOING = "true";
        final static String NOT_GOING = "false";
        public static Firebase ref = DataHolder.getRef();

        //Example Use:      RSVP.rsvp("event1", RSVP.GOING);
        public static void rsvp(String eventID, String state) {
            if (state == GOING) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(DataHolder.getUID(), state);
                ref.child("calendar").child(eventID).child("rsvp").updateChildren(map);
            } else if (state == NOT_GOING) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ref.child("calendar").child("event1").child("rsvp").child(DataHolder.getUID().toString()).removeValue();
                    }
                });
                t.start();
            }
        }

        public static boolean isGoing(String eventID) {
            return EventItem.allEvents.get(eventID).getRSVP().containsKey(DataHolder.getUID());
        }
}
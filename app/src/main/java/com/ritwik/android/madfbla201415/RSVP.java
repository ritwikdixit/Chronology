package com.ritwik.android.madfbla201415;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class RSVP {
        final static String GOING = "true";
        final static String NOT_GOING = "false";

        public static Firebase ref = DataHolder.getRef();
        public static void rsvp(String eventID, String state){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(DataHolder.getUID(), "true");
            ref.child(eventID).updateChildren(map);
        }

        public static int getRSVPState(String eventID){
            Map<String, Object> map = new HashMap<String, Object>();
            if(!map.containsKey(DataHolder.getUID())) return 0;

            if(map.get(DataHolder.getUID()) == GOING) return 1;
            else return -1;
        }
}
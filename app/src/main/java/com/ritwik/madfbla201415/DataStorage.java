package com.ritwik.madfbla201415;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataStorage {
   public static void writeToFile(ArrayList<EventItem> events, Context c) {
        try {
            String s = createStorageString(events);
            FileOutputStream fos = c.openFileOutput(DataHolder.FILE_NAME, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
            Log.v(HomepageFragment.DATA_TAG, "Success11");
        } catch (IOException e) {
            Log.v(HomepageFragment.DATA_TAG, e.getMessage());
        }
    }

    public static ArrayList<EventItem> readFromFile(Context c) {
        ArrayList<EventItem> events = new ArrayList<>();
        try {
            FileInputStream fos = c.openFileInput(DataHolder.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fos);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            events = parse(sb.toString(), c);
            Log.v(HomepageFragment.DATA_TAG, "Success22");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    //create 1 long string from the events
    public static String createStorageString(ArrayList<EventItem> events) {
        String s = "";
        for (EventItem thisEvent : events)
            s += thisEvent.toString();
        return s;
    }

    //parse the string to an array of events
    public static ArrayList<EventItem> parse(String loaded, Context context) {
        ArrayList<EventItem> events = new ArrayList<>();
        String[] eventStrings = loaded.split("<*>");
        for (String eventStr : eventStrings)
            events.add(new EventItem(eventStr.split("---"), context));
        return events;
    }
}
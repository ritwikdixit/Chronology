package com.ritwik.madfbla201415.Push;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ritwik on 4/17/2015.
 */
public class PushStorage {

    public static final String PUSH_FILE_NAME = "chronology_notifications01";

    public static void writeToFile(ArrayList<PushItem> pushes, Context c) {
        try {
            String s = createStorageString(pushes);
            Log.v(PushActivity.LOG_TAG, "write events are: " + s);
            FileOutputStream fos = c.openFileOutput(PUSH_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {}
    }

    public static ArrayList<PushItem> readFromFile(Context c) {
        ArrayList<PushItem> pushes = new ArrayList<>();
        try {
            FileInputStream fos = c.openFileInput(PUSH_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fos);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            pushes = parse(sb.toString(), c);
        } catch (Exception e) {
            Log.v(PushActivity.LOG_TAG, e.getMessage());
        }

        return pushes;
    }

    //create 1 long string from the notifications
    public static String createStorageString(ArrayList<PushItem> pushes) {
        String s = "";
        for (PushItem thisPush : pushes)
            s += thisPush.toString();
        return s;
    }

    //parse the string back into an array of events
    public static ArrayList<PushItem> parse(String loaded, Context context) {

        Log.v(PushActivity.LOG_TAG, "parsing: " + loaded);
        ArrayList<PushItem> pushes = new ArrayList<>();
        String[] pushStrings = loaded.split("&&&");
        for (String pushStr : pushStrings) {
            pushes.add(new PushItem(pushStr.split("::")));
        }
        return pushes;
    }


}

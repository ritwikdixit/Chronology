package com.ritwik.android.madfbla201415;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.firebase.client.Firebase;
import com.pushbots.push.Pushbots;
import com.ritwik.android.madfbla201415.Database.DataModel;

/**
 * Created by Soham Pardeshi on 12/31/2014.
 */
public class DataHolder {

    private static Firebase ref;
    private static String UID, fullName, email, phoneNumber;
    private static String[] mDrawerArray = { "Home", "Month View",
            "All Events", "Help", "Notifications", "Settings", "Log Out",
            "Admin [beta]"};

    private DataHolder(){} //Empty Constructor

    public static void setRef(Firebase r){
        ref = r;
    }
    public static Firebase getRef(){
        return ref;
    }
    public static boolean isNull(){
        return ref == null;
    }
    public static void setUID(String x){
        UID = x;
    }
    public static void setName(String x){ fullName = x; }
    public static void setEmail(String x){
        email = x;
    }
    public static void setPhoneNumber(String x){
        phoneNumber = x;
    }
    public static String getUID(){
        return UID;
    }
    public static String getName(){
        return fullName;
    }
    public static String getEmail(){
        return email;
    }
    public static String getPhoneNumber(){
        return phoneNumber;
    }
    public static boolean hasUserData(){
        return fullName != null && phoneNumber != null &&
                email != null && !fullName.trim().equals("")
                && !email.trim().equals("") && !phoneNumber.trim().equals("");
    }
    public static String[] getDrawerArray() {
        return mDrawerArray;
    }
}

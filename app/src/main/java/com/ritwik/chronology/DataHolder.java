package com.ritwik.chronology;

import com.firebase.client.Firebase;
import com.pushbots.push.Pushbots;


public class DataHolder {

    private static Firebase ref;
    private static String UID, fullName, email, phoneNumber;
    private static String[] mUserDrawerArray = { "Home", "Month View",
            "All Events", "Help", "Notifications", "Log Out"};
    private static String[] mAdminDrawerArray = { "Home", "Month View",
            "All Events", "Help", "Notifications", "Create Event", "Log Out"};

    public static final String FILE_NAME = "stored_events";

    private static boolean isAdmin = false;
    private static int[] resIds = {
            R.drawable.ic_home,
            R.drawable.ic_month,
            R.drawable.ic_all_events,
            R.drawable.ic_help,
            R.drawable.ic_notifications,
            R.drawable.ic_add_event,
            R.drawable.ic_log_out
    };

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
    public static void setName(String x){
        fullName = x;
        Pushbots.sharedInstance().setAlias(x);
    }
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
        if(isAdmin)
            return mAdminDrawerArray;
        else
            return mUserDrawerArray;
    }
    public static void setAdmin(boolean x){
        isAdmin = x;
    }
    public static boolean isAdmin(){
        return isAdmin;
    }
    public static void resetData(){
        isAdmin = false;
        fullName = email = phoneNumber = UID = "";
    }
    //input -1 for settings, make sure it is last in the array
    public static int getDrawerIdForIndex(int index) {

        return resIds[index];

    }
}
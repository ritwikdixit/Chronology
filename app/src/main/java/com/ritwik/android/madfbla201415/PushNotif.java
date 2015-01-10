package com.ritwik.android.madfbla201415;
import com.pushbots.push.Pushbots;
import android.app.Application;
import android.util.Log;

public class PushNotif extends Application {
    public static final String Notif_App_ID = "54a346da1d0ab1b55e8b45fc";
    public static final String Notif_Sender_ID = "901400243848";

    @Override
    public void onCreate() {
        super.onCreate();
        Pushbots.init(this, Notif_Sender_ID, Notif_App_ID);
        Pushbots.getInstance().setMsgReceiver(PushReceiver.class);
    }
}
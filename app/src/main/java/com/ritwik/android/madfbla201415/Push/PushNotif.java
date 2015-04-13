package com.ritwik.android.madfbla201415.Push;
import com.pushbots.push.Pushbots;
import com.ritwik.android.madfbla201415.DataHolder;

import android.app.Application;

public class PushNotif extends Application {
    public static final String Notif_App_ID = "54a346da1d0ab1b55e8b45fc";
    public static final String Notif_Sender_ID = "901400243848";

    @Override
    public void onCreate() {
        super.onCreate();
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(PushReceiver.class);
        if(DataHolder.getName() != null)
            Pushbots.sharedInstance().setAlias(DataHolder.getName());

    }
}
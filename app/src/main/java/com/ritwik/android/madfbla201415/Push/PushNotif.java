package com.ritwik.android.madfbla201415.Push;
import com.pushbots.push.Pushbots;
import com.ritwik.android.madfbla201415.DataHolder;
import com.ritwik.android.madfbla201415.HomepageFragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class PushNotif extends Application {
    public static final String Notif_App_ID = "54a346da1d0ab1b55e8b45fc";
    public static final String Notif_Sender_ID = "901400243848";

    @Override
    public void onCreate() {
        super.onCreate();
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(PushReceiver.class);

    }
}
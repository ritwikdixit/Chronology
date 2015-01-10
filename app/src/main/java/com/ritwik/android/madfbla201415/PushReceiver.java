package com.ritwik.android.madfbla201415;

/**
 * Created by Soham Pardeshi on 1/9/2015.
 */

import java.util.HashMap;
import com.pushbots.push.Pushbots;
import com.ritwik.android.madfbla201415.Database.PushModel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver
{
    private static final String TAG = "customPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);
        // Handle Push Message when opened
        if (action.equals(Pushbots.MSG_OPENED)) {
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(Pushbots.MSG_OPEN);
            String message = PushdataOpen.get("message").toString();
            String details = "No further details";
            if(PushdataOpen.containsKey("details"))
                details = PushdataOpen.get("details").toString();
            if(!LoadingActivity.isActive()) {

                Intent startAppIntent = new Intent(Intent.ACTION_MAIN);
                startAppIntent.setClass(Pushbots.getInstance().appContext, LoadingActivity.class);
                startAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Pushbots.getInstance().appContext.startActivity(startAppIntent);


                Intent viewPushIntent = new Intent(Pushbots.getInstance().appContext, PushActivity.class);
                viewPushIntent.putExtra("Push_Details", details);
                viewPushIntent.putExtra("Push_Message", message);
                viewPushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                Pushbots.getInstance().appContext.startActivity(viewPushIntent);

                PushModel newPush = new PushModel(System.currentTimeMillis(), message, details);
                newPush.save();

            }
        // Handle Push Message when received
        }else if(action.equals(Pushbots.MSG_RECEIVE)){
            //They received the message
        }
    }
}
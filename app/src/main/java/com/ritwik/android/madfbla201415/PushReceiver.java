package com.ritwik.android.madfbla201415;

/**
 * Created by Soham Pardeshi on 1/9/2015.
 */

import java.util.HashMap;
import com.pushbots.push.Pushbots;
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
            if(!LoadingActivity.isActive()) {
                Intent startAppIntent = new Intent(Intent.ACTION_MAIN);
                startAppIntent.setClass(Pushbots.getInstance().appContext, LoadingActivity.class);
                startAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Pushbots.getInstance().appContext.startActivity(startAppIntent);


                Intent viewPushIntent = new Intent(Pushbots.getInstance().appContext, PushActivity.class);
                viewPushIntent.putExtra("Push_Message", PushdataOpen.get("message").toString());
                viewPushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Pushbots.getInstance().appContext.startActivity(viewPushIntent);


            }
            //TODO: Remove below code. Testing only.
            Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

        // Handle Push Message when received
        }else if(action.equals(Pushbots.MSG_RECEIVE)){
            //They received the message
        }
    }
}
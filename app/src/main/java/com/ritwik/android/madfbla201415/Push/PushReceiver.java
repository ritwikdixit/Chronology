package com.ritwik.android.madfbla201415.Push;

/**
 * Created by Soham Pardeshi on 1/9/2015.
 */

import java.util.HashMap;
import com.pushbots.push.Pushbots;
import com.ritwik.android.madfbla201415.Database.PushModel;
import com.ritwik.android.madfbla201415.LoadingActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver
{

    public static final String PUSH_DETAILS_KEY = "Push_Details";
    public static final String PUSH_MSG_KEY = "Push_Message";
    public static final String PUSH_REDIRECT_KEY = "Push";

    public static final String TAG = "customPushReceiver";
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
                startAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

                //giving redirect data
                startAppIntent.putExtra(PUSH_REDIRECT_KEY, true);
                startAppIntent.putExtra(PUSH_DETAILS_KEY, details);
                startAppIntent.putExtra(PUSH_MSG_KEY, message);
                Pushbots.getInstance().appContext.startActivity(startAppIntent);

                PushModel newPush = new PushModel(System.currentTimeMillis(), message, details);
                newPush.save();

            }
        // Handle Push Message when received
        }else if(action.equals(Pushbots.MSG_RECEIVE)){
            //They received the message
        }
    }
}
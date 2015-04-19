package com.ritwik.madfbla201415.Push;

/**
 * Created by Soham Pardeshi on 1/9/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;
import com.ritwik.madfbla201415.LoadingActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PushReceiver extends BroadcastReceiver
{

    public static final String PUSH_DETAILS_KEY = "Push_Details";
    public static final String PUSH_MSG_KEY = "Push_Message";
    public static final String PUSH_REDIRECT_KEY = "Push";

    public static final String PUSHBOTS_OPEN_MSG = "message";
    public static final String PUSHBOTS_OPEN_DETAILS = "details";

    public static final String TAG = "customPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);
        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(Pushbots.MSG_OPEN);
            String message = PushdataOpen.get(PUSHBOTS_OPEN_MSG).toString();
            String details = "No further details.";
            if(PushdataOpen.containsKey(PUSHBOTS_OPEN_DETAILS))
                details = PushdataOpen.get(PUSHBOTS_OPEN_DETAILS).toString();
            if(!LoadingActivity.isActive()) {

                Intent startAppIntent = new Intent(Intent.ACTION_MAIN);
                startAppIntent.setClass(context, LoadingActivity.class);
                startAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

                //giving redirect data
                startAppIntent.putExtra(PUSH_REDIRECT_KEY, true);
                startAppIntent.putExtra(PUSH_DETAILS_KEY, details);
                startAppIntent.putExtra(PUSH_MSG_KEY, message);
                Pushbots.sharedInstance().startActivity(startAppIntent);

            }
        // Handle Push Message when received
        }else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){
            //They received the message
        }
    }
}
package com.ritwik.chronology.Push;


import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soham Pardeshi on 1/18/2015.
 */

public class SendPush {
    public static final String Notif_App_ID = "54a346da1d0ab1b55e8b45fc";
    public static final String Notif_Sender_ID = "901400243848";
    public static final String Notif_Secret_ID = "f7cd90b79d6a801795e6866ba1d3a7fc";
    public static void postData(String title, String details) throws UnsupportedEncodingException {
        // Create a new HttpClient and Post Header
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost("https://api.pushbots.com/push/all");


        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("msg", title ));
        nameValuePairs.add(new BasicNameValuePair("details", details));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httppost.setHeader("X-PUSHBOTS-APPID:", Notif_App_ID);
        httppost.setHeader("X-PUSHBOTS-SECRET:", Notif_Secret_ID);
        httppost.setHeader("Content-Type:", "application/json");
        httppost.setHeader("Content-Length: ", nameValuePairs.toString().length() + "");
        String json = "{ \"notification\" : {  \"android\" : { \"msg\" : \"" + title + "\",\"details\" : \"" + details + "\"} }";


        // Execute HTTP Post Request

        Log.e("Tomoto", "Beginning");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    Log.e("Tomoto", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Log.e("Tomoto", "Ending");
    }
}

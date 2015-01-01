package com.ritwik.android.madfbla201415;

import com.firebase.client.Firebase;

/**
 * Created by Soham Pardeshi on 12/31/2014.
 */
public class DataHolder {

    private static Firebase ref;
    private DataHolder(){} //Empty Constructor

    public static void setRef(Firebase r){
        ref = r;
    }
    public static Firebase getRef(){
        return ref;
    }
}

package com.ritwik.android.madfbla201415.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.firebase.client.Firebase;

/**
 * Created by Soham Pardeshi on 1/11/2015.
 */

@Table(name = "System_Save")
public class DataModel extends Model {
    @Column(name = "Firebase")
    public Firebase ref;

    public DataModel(){
        super();
    }

    public DataModel(Firebase ref){
        super();
        this.ref = ref;
    }
}
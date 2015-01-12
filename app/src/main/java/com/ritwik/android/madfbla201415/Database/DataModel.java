package com.ritwik.android.madfbla201415.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.firebase.client.Firebase;

/**
 * Created by Soham Pardeshi on 1/11/2015.
 */

@Table(name = "Events")
public class DataModel extends Model {
    @Column(name = "Contact")
    public String contact;
    @Column(name = "Details")
    public String details;
    @Column(name = "End_Date")
    public String endDate;
    @Column(name = "End_Time")
    public String endTime;
    @Column(name = "myID")
    public String id;
    @Column(name = "Location")
    public String location;
    @Column(name = "Start_Date")
    public String startDate;
    @Column(name = "Start_Time")
    public String startTime;
    @Column(name = "Title")
    public String title;
    @Column(name = "URL")
    public String url;

    public DataModel(){
        super();
    }
    public DataModel(String startDate, String endDate, String startTime,String endTime, String title,
                     String location, String details, String url, String contact, String id){
        super();
        this.contact = contact;
        this.details = details;
        this.endDate = endDate;
        this.endTime = endTime;
        this.id = id;
        this.location = location;
        this.startDate = startDate;
        this.startTime = startTime;
        this.title = title;
        this.url = url;
    }
}
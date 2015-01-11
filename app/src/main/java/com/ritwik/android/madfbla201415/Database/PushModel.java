package com.ritwik.android.madfbla201415.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Push_Table")
public class PushModel extends Model {
    @Column(name = "Time")
    public long time;

    @Column(name = "Name")
    public String name;

    @Column(name = "Details")
    public String details;

    public PushModel(){
        super();
    }
    public PushModel(long time, String name, String details){
        super();
        this.time = time;
        this.name = name;
        this.details = details;
    }
}
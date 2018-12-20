package com.example.huy.myfirstapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "time")
    private String appointedTime;
    @ColumnInfo(name = "status")
    private String status;

    public Task(String description, String appointedTime, String status) {
        this.description = description;
        this.appointedTime = appointedTime;
        this.status = status;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getAppointedTime() {
        return appointedTime;
    }

    public String getStatus() {
        return status;
    }
}

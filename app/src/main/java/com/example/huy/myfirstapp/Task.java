package com.example.huy.myfirstapp;

public class Task {
    private String description;
    private String appointedTime;

    public Task(String description, String appointedTime) {
        this.description = description;
        this.appointedTime = appointedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointedTime() {
        return appointedTime;
    }

    public void setAppointedTime(String appointedTime) {
        this.appointedTime = appointedTime;
    }
}

package com.example.huy.myfirstapp;

public class Task {
    private String description;
    private String appointedTime;
    private String isChecked;

    public Task(String description, String appointedTime, String isChecked) {
        this.description = description;
        this.appointedTime = appointedTime;
        this.isChecked = isChecked;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
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

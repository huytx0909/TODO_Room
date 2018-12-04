package com.example.huy.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestingActivity extends AppCompatActivity {
//TODO Create a notification, get task info from the Intent
    //TODO this activity will be created multiple time, there is no need to loop in this.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
    }
}

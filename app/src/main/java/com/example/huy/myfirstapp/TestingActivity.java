package com.example.huy.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class TestingActivity extends AppCompatActivity {
//TODO Create a notification, get task info from the Intent
    //TODO this activity will be created multiple time, there is no need to loop in this.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Intent getInfoForNoti = getIntent();
        String time = getInfoForNoti.getExtras().getString("time");
        String description = getInfoForNoti.getExtras().getString("description");
        Button button = findViewById(R.id.testing_Button);
        button.setText(time);
    }
}

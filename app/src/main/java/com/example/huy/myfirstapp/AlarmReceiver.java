package com.example.huy.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("alarm_message");

            Intent intent1 = new Intent(context, TestingActivity.class);
            intent1.putExtra("alarm_message", message);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

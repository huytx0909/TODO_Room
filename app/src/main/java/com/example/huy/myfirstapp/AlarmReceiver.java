package com.example.huy.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("time");
            Log.e("TIME", message);
            Intent sendToNoti = new Intent(context, TestingActivity.class);
            sendToNoti.putExtra("time", message);
            sendToNoti.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sendToNoti);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

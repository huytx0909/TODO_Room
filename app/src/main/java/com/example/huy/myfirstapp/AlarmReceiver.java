package com.example.huy.myfirstapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    String time;
    String description;
    int notificationId;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();
            time = bundle.getString("time");
            description = bundle.getString("description");
            Log.e("TIME", time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "myNoti", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);

        Intent goToHome = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, goToHome, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.task_manager)
                .setTicker("It's time")
                .setContentTitle(time.substring(11))
                .setContentText(description)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent);

        Log.e("TEST", "code ran here!");

        notificationManager.notify((int) (Math.random() * 100 + 1), notificationBuilder.build());

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isInteractive = pm.isInteractive();
        Log.e("screen on.................................", "" + isInteractive);
    }
}


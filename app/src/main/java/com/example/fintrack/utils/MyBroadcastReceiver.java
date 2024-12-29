package com.example.fintrack.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fintrack.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBroadcastReceiver", "Alarm triggered at midnight");

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "daily_budget_channel")
                .setSmallIcon(R.mipmap.ic_launcher) // Replace with your app's icon
                .setContentTitle("Daily Budget Update")
                .setContentText("Check your daily budget updates!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("No permisiion","Notification");
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}

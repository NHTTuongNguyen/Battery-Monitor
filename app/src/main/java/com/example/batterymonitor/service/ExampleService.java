package com.example.batterymonitor.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.batterymonitor.MainActivity;
import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.HomeActivity;

import java.io.FileInputStream;

import static com.example.batterymonitor.activity.App.CHANNEL_ID;

public class ExampleService extends Service {
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
        String input = "Hello Im a Android";
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Example Service")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.ic_baseline_android_24)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }







}

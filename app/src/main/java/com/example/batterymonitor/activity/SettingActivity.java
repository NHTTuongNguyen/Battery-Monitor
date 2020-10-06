package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.batterymonitor.R;
import static com.example.batterymonitor.activity.App.CHANNEL_ID;


public class SettingActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout_Rate;
    private ImageView imgSettingCancel;
    private final String URL_Rate = "https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi";
    private Switch switchNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        relativeLayout_Rate = findViewById(R.id.relativeLayout_Rate);
        imgSettingCancel = findViewById(R.id.imgSettingCancel);
        switchNotification = findViewById(R.id.switchNotification);
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Log.d("RRR","dccheicj");
//                    String message = "This is a notification example.";
//
//                    Intent notificationIntent = new Intent(SettingActivity.this,HomeActivity.class);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                        NotificationChannel serviceChannel = new NotificationChannel(CHANGE_ID,getString(R.string.app_name),NotificationManager.IMPORTANCE_DEFAULT);
//
//                    }
//                    NotificationCompat.Builder   builder  =  new NotificationCompat.Builder(
//                            SettingActivity.this)
//                            .setSmallIcon(R.drawable.ic_baseline_android_24)
//                            .setContentTitle("New Notification")
//                            .setContentText(message)
//                            .setAutoCancel(true);
//                    Intent intent = new Intent(SettingActivity.this,HomeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(SettingActivity.this,
//                            0, intent, 0);
//                    builder.setContentIntent(pendingIntent);
//                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(0,builder.build());

                }else {
                    Log.d("RRR","k dc ");
                }
            }
        });
        imgSettingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relativeLayout_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent("https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi");
            }
        });
    }
    private void setActionIntent(String actionIntent) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(actionIntent));
        startActivity(i);
    }
}
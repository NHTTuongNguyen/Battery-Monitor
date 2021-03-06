package com.example.batterymonitor.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.HomeActivity;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.example.batterymonitor.activity.App.CHANNEL_ID;

public class ServiceNotifi extends Service {
    private Context context;
    private WindowManager mWindowManager;
    private View mChatHeadView;
    private ScheduledFuture mHandle;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private SharedPreference_Utils sharedPreference_utils;
    public ServiceNotifi() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initChat();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int input = intent.getIntExtra("message",0);
        Log.d("get_message",input+"");
        sharedPreference_utils= new SharedPreference_Utils(this);
        if (sharedPreference_utils != null) {
            if (input == 59) {
            }
        }
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_android_circle)) // set a png or jpg images
                    .setSmallIcon(R.drawable.ic_baseline_android_24)
                    .setContentTitle(input+"h")
                    .setContentText(getString(R.string.BatteryMonitorRunning))
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);



        //do heavy work on a background thread
//        stopSelf();
        return START_NOT_STICKY;
//        sharedPreference_utils = new SharedPreference_Utils(this);
//        final Runnable deleteIt = new Runnable() {
//            public void run() {
//
//                Log.d("timeDelay","run");
//                sharedPreference_utils.removeSaveBatteryThan24h();
////                getSharedPreferences("pref_file", 0).edit().clear().commit();
////                mHandle.cancel(false); //don't cancel here if you want it to run every 24 hours
//            }
//        };
//        if(mHandle == null)
//            mHandle = scheduler.scheduleAtFixedRate(deleteIt,  60 * 60 * 24, 60 * 60 * 24, TimeUnit.SECONDS);
//        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void initChat() {
        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);

        //Set the close button.
        ImageView closeButton = (ImageView) mChatHeadView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the service and remove the chat head from the window
                stopSelf();
            }
        });

        //Drag and move chat head using user's touch action.
        final TextView chatHeadImage = (TextView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
//        float input = intent.getFloatExtra("inputExtra",0);
        chatHeadImage.setText("78");
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;
                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.
//                            Intent intent = new Intent(ExampleService.this, HomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            //close the service and remove the chat heads
//                            stopSelf();
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }






}

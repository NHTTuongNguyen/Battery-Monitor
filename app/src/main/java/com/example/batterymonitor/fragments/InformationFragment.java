package com.example.batterymonitor.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.receiver.BatteryReceiverClass;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class InformationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private LinearLayout linearLayout_View,
            linearLayout_SettingWifi,
            linearLayout_Bluetooth,linearLayout_Brightness,linearLayout_Landscape,linearLayout_WifiOnOFF;
    private  final String url_Battery= Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
    private final String url_SettingWifi = Settings.ACTION_WIFI_SETTINGS;
    SharedPreference_Utils sharedPreference_utils;
    private ImageView imageView_Bluetooth,imageView_Brightness,imageView_Landscape,imageView_WifiOnOff;
    private View view;
    private LineChartView lineChartView;
    private TextView txtCurrentTimeThread,txtCurrentDaysThread,txtCurrentMemoryThread;



    private BatteryReceiverClass batteryReceiverClass;
    private IntentFilter intentFilter_ACTION_BATTERY_CHANGED,intentFilter_ACTION_STATE_CHANGED;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_information, container, false);
        initView();
        sharedPreference_utils = new SharedPreference_Utils(getActivity());
        batteryReceiverClass = new BatteryReceiverClass();
        intentFilter_ACTION_BATTERY_CHANGED = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter_ACTION_STATE_CHANGED = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        linearLayout_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent(url_Battery);
            }
        });
        linearLayout_SettingWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent(url_SettingWifi);
            }
        });
        linearLayout_Bluetooth.setOnClickListener(new View.OnClickListener() {
            boolean trues;
            @Override
            public void onClick(View view) {
//                trues = !trues;
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter.isEnabled()){
                    imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                    adapter.disable();
                } else {
                    imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24);
                    adapter.enable();
                }

//                    if (trues) {
////                        SharedPreference_Utils.setBluetooth_Turn_On(R.drawable.ic_baseline_bluetooth_24);
////                        imageView_Bluetooth.setImageResource(sharedPreference_utils.getBluetooth_Turn_ON());
//
//                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24);
//                    } else {
//
////                        SharedPreference_Utils.setBluetooth_OFF(R.drawable.ic_baseline_bluetooth_disabled_24);
////                        imageView_Bluetooth.setImageResource(sharedPreference_utils.getBluetooth_Turn_Off());
//
//                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
//
//                    }

            }
        });
        linearLayout_Brightness.setOnClickListener(new View.OnClickListener() {
            boolean brightness;
            @Override
            public void onClick(View view) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                   if (Settings.System.canWrite(getActivity())){
                       brightness = !brightness;
                       if (brightness){
                           Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                           imageView_Brightness.setImageResource(R.drawable.ic_baseline_brightness_auto_24);
                       }else {
                           Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                           imageView_Brightness.setImageResource(R.drawable.ic_baseline_brightness_7_24);
                       }
                   }else {
                       alertDialogPermission();


                   }
               }
            }
        });
        linearLayout_Landscape.setOnClickListener(new View.OnClickListener() {
            boolean landscape;
            @Override
            public void onClick(View view) {
                landscape = !landscape;
                if (landscape){

                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_rotation_24);
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }
                else {
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_lock_rotation_24);
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }
            }
        });
        linearLayout_WifiOnOFF.setOnClickListener(new View.OnClickListener() {
            boolean wifi;
            @Override
            public void onClick(View view) {
                wifi = !wifi;
                if (wifi){
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_off_24);
                    SharedPreference_Utils.setWifi(R.drawable.ic_baseline_signal_wifi_off_24);

                }else {
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_4_bar_24);

                }
            }
        });
        initChart();

        displayCurrentTime();
        return view;
    }

    private  void alertDialogPermission() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Permission write settings !");
        alertDialogBuilder
                .setMessage("Allow permission write settings for your system ?")
                .setCancelable(false)
                .setPositiveButton("ALLOW",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void displayCurrentTime() {
        Thread myThread;
        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();
    }
    private void doWork() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try{
                    txtCurrentTimeThread = view.findViewById(R.id.txtCurrentTimeThread);
                    txtCurrentDaysThread = view.findViewById(R.id.txtCurrentDaysThread);


                    ////////////////////
//                    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//                    ActivityManager activityManager = (ActivityManager)getActivity(). getSystemService(Context.ACTIVITY_SERVICE);
//                    activityManager.getMemoryInfo(mi);
//                    long availableMegs = mi.availMem / 1048576L;
//                    long percentAvail = mi.availMem / mi.totalMem;
//                    Log.d("availableMegs",availableMegs+"");
//                    Log.d("percentAvail",percentAvail+"");

//                    txtCurrentMemoryThread.setText(availableMegs+"");

                    //////////////////
                    Calendar calendar = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                    txtCurrentDaysThread.setText(currentDate);
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours+ "h"  + ":" + minutes+ "m" + ":" + seconds+ "s";
                    txtCurrentTimeThread.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }


    private class CountDownRunner implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    private void initView() {
        lineChartView = view.findViewById(R.id.chart);
        imageView_Bluetooth = view.findViewById(R.id.img_Bluetooth);
        imageView_Landscape = view.findViewById(R.id.img_Landscape);
        imageView_Brightness = view.findViewById(R.id.img_Brightness);
        imageView_WifiOnOff = view.findViewById(R.id.img_WifiOnOff);
        linearLayout_View =view.findViewById(R.id.linearLayout_View);
        linearLayout_Landscape = view.findViewById(R.id.linearLayout_Landscape);
        linearLayout_SettingWifi = view.findViewById(R.id.linearLayout_SettingWifi);
        linearLayout_Bluetooth = view.findViewById(R.id.linearLayout_Bluetooth);
        linearLayout_Brightness = view.findViewById(R.id.linearLayout_Brightness);
        linearLayout_WifiOnOFF = view.findViewById(R.id.linearLayout_WifiOnOFF);
    }
    private void initChart() {
        String[] axisDataX =
                {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

        int[] axisDataY = {15, 10, 50, 25, 40, 35, 30, 45, 50, 55, 63, 65};
        List axisValuesX = new ArrayList();
        List axisValuesY = new ArrayList();

        Line line = new Line(axisValuesY);


        for(int i = 0; i < axisDataX.length; i++){
            axisValuesX.add(i, new AxisValue(i).setLabel(axisDataX[i]));
            Log.d("axisDataX",axisValuesX.get(i)+"");

        }

        for (int i = 0; i < axisDataY.length; i++){
            axisValuesY.add(new PointValue(i, axisDataY[i]));
        }

        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView.setLineChartData(data);


        Axis axis = new Axis();
        axis.setValues(axisValuesX);
        data.setAxisXBottom(axis);
        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);

        Line linessss = new Line(axisValuesY);
        linessss.setColor(R.color.colorPrimary);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));

        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);

        yAxis.setName("Sales in millions");
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top =70;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
    private void setActionIntent(String actionIntent) {
        Intent intent = new Intent();
        intent.setAction(actionIntent);
        getActivity().startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_STATE_CHANGED);
        imageView_WifiOnOff.setImageResource(sharedPreference_utils.getWifi());

    }
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiverClass);
        super.onPause();
    }
}
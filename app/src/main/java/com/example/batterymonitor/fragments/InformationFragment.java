package com.example.batterymonitor.fragments;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.adapters.ChartsAdapter;
import com.example.batterymonitor.models.ChartsModel;
import com.example.batterymonitor.receiver.BatteryReceiverClass;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
import lecho.lib.hellocharts.view.LineChartView;


public class InformationFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static  final String TAG= "InformationFragment";
    private LineChart lineChart;
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
    private SharedPreference_Utils sharedPreference_utils;
    private ImageView imageView_Bluetooth,imageView_Brightness,imageView_Landscape,imageView_WifiOnOff;
    private View view;
    private TextView txtCurrentTimeThread;
    private TextView txtCurrentDaysThread;
    private TextView txtCurrentMemoryThread;
    private TextView txtTemp;
    private BatteryReceiverClass batteryReceiverClass;
    private IntentFilter intentFilter_ACTION_BATTERY_CHANGED,
            intentFilter_ACTION_STATE_CHANGED,
            intentFilter_WIFI_STATE_CHANGED_ACTION;
    private Button btnViewBattery;
    private BarChart barChart;
    private LineChart lineChartTest;
    private int mFillColor = Color.argb(150,51,181,229);
    private ListView listView;
    private String curTimeSharePre;
    private ArrayList<ChartsModel> chartsList;
    private ChartsAdapter chartsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreference_utils = new SharedPreference_Utils(getActivity());
        view =  inflater.inflate(R.layout.fragment_information, container, false);
        initView();
        batteryReceiverClass = new BatteryReceiverClass();
        btnViewBattery = view.findViewById(R.id.btnViewBattery);
        intentFilter_ACTION_BATTERY_CHANGED = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter_ACTION_STATE_CHANGED = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter_WIFI_STATE_CHANGED_ACTION = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);

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
                if (adapter == null){
                    Toast.makeText(getActivity(), "Your phone does not support bluetooth", Toast.LENGTH_SHORT).show();
                }else {
                    if (adapter.isEnabled()){
                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                        adapter.disable();
                    } else {
                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);
                        adapter.enable();
                    }
                }


//

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
                           imageView_Brightness.setImageResource(R.drawable.ic_baseline_brightness_default);
                       }
                   }else {
                       alertDialogPermission();


                   }
               }
            }
        });
        eventScreen_ORIENTATION();

        linearLayout_Landscape.setOnClickListener(new View.OnClickListener() {
            boolean landscape;
            @Override
            public void onClick(View view) {
                landscape = !landscape;
                if (landscape){
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_rotation_24);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else {
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_lock_rotation_24);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
//                    SharedPreference_Utils.setWifi(R.drawable.ic_baseline_signal_wifi_off_24);
                }else {
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_default);
                }
            }
        });
        btnViewBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent(url_Battery);
            }
        });

        ///////////////
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreference_Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreference_Utils.SaveBattery, null);
        Type type = new TypeToken<ArrayList<ChartsModel>>(){}.getType();
        chartsList = gson.fromJson(json, type);
        Log.d("get_tasksssslist",String.valueOf(json));
        if (chartsList == null) {
            chartsList = new ArrayList<>();
        }
        chartsAdapter = new ChartsAdapter(getActivity(), chartsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chartsAdapter);
        chartsAdapter.notifyDataSetChanged();
        for (int i = 0; i< chartsList.size(); i++){
            Log.d("chartsModel", chartsList.get(i).getLevelBattery()+"");
        }

        initChart2();
        chartTest();
        displayCurrentTime();
        return view;
    }
    private void chartTest() {
        LineChartView lineChartView = view.findViewById(R.id.chart);
//        sharedPreference_utils.getSaveHours();




//        List<ChartsModel> axisData = new ArrayList<>();
//        axisData.add(new ChartsModel("Jan"));
//        axisData.add(new ChartsModel("Feb"));
//        axisData.add(new ChartsModel("Mar"));
//        axisData.add(new ChartsModel("Apr"));
//        axisData.add(new ChartsModel("May"));
//        axisData.add(new ChartsModel("June"));
//        axisData.add(new ChartsModel("July"));
//        axisData.add(new ChartsModel("Aug"));
//        axisData.add(new ChartsModel("Sept"));
//        axisData.add(new ChartsModel("Oct"));
//        axisData.add(new ChartsModel("Nov"));
//        axisData.add(new ChartsModel("Dec"));
        List<String> axisData = new ArrayList<>();
        axisData.add("Jan");
        axisData.add("Feb");
        axisData.add("Mar");
        axisData.add("Apr");
        axisData.add("May");
        axisData.add("June");
        axisData.add("July");
        axisData.add("Aug");
        axisData.add("Sept");
        axisData.add("Oct");
        axisData.add("Nov");
        axisData.add("Dec");


        List<Integer> yAxisData = new ArrayList<>();
        yAxisData.add(70);
        yAxisData.add(20);
        yAxisData.add(15);
        yAxisData.add(30);
        yAxisData.add(20);
        yAxisData.add(60);
        yAxisData.add(15);
        yAxisData.add(40);
        yAxisData.add(45);
        yAxisData.add(10);
        yAxisData.add(90);
        yAxisData.add(18);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues);
        line.setColor(Color.RED);
        line.setColor(R.color.colorGreen);

        for(int i = 0; i < axisData.size(); i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i)));
        }

        for (int i = 0; i < yAxisData.size(); i++){
            yAxisValues.add(new PointValue(i, yAxisData.get(i)));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        lineChartView.setLineChartData(data);
        Axis axis = new Axis();

        axis.setValues(axisValues);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);
    }
    private void initChart2() {
        lineChart  = view.findViewById(R.id.line_Charts);
        List<Entry> yValues = new ArrayList<>();

        for (int i = 0; i< chartsList.size(); i++){
            Log.d("chartsModel_SIZE", chartsList.get(i).getLevelBattery()+"");
            int Y = Integer.parseInt(chartsList.get(i).getHours());
            int X = chartsList.get(i).getLevelBattery();
            yValues.add(new Entry(Y,X));

        }
//        yValues.add(new Entry(17,100));
//        yValues.add(new Entry(20,80));
//        yValues.add(new Entry(24,60));
        LineDataSet set1 = new LineDataSet(yValues,"Data Battery");
        set1.notifyDataSetChanged();
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.LTGRAY);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);
//        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.CYAN);
        set1.setFillAlpha(80);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data  = new LineData(dataSets);
        Drawable drawable  = ContextCompat.getDrawable(getActivity(),R.drawable.gradient_charts);
        set1.setFillDrawable(drawable);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setData(data);
    }

    private void eventScreen_ORIENTATION() {
        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_rotation_24);
                Log.d("YYY","ORIENTATION_LANDSCAPE:");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_lock_rotation_24);
                Log.d("YYY","ORIENTATION_PORTRAIT:");
                // handle portrait here
                break;
            default:
        }
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
    private void setRunOnUiThread() {
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
//
//
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
                    curTimeSharePre = hours+ "h"  + ":" + minutes+ "m";
                    txtCurrentTimeThread.setText(curTime);
//                    sharedPreference_utils.setSaveHours(curTimeSharePre);

                }catch (Exception e) {}
            }
        });
    }

    private class CountDownRunner implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    setRunOnUiThread();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView_Charts);
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
//        txtTemp = view.findViewById(R.id.txtNhietDoLon);

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
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_WIFI_STATE_CHANGED_ACTION);
//        int test =  sharedPreference_utils.getSaveBattery();
//        Log.d("test",test+"");
    }
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiverClass);
        super.onPause();
    }
}
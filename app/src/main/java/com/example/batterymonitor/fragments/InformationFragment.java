package com.example.batterymonitor.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.Common.Common;
import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.HomeActivity;
import com.example.batterymonitor.adapters.ChartsAdapter;
import com.example.batterymonitor.models.ChartsModel;
import com.example.batterymonitor.receiver.BatteryReceiverClass;
import com.example.batterymonitor.service.ServiceNotifi;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.facebook.ads.Ad;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class InformationFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static  final String TAG= "InformationFragment";
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private LineChart lineChart;
    private  final String url_Battery= Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
    private final String url_SettingWifi = Settings.ACTION_WIFI_SETTINGS;
    private SharedPreference_Utils sharedPreference_utils;
    private ImageView imageView_Bluetooth,imageView_Brightness,imageView_Landscape,imageView_WifiOnOff;
    private View view;
    private TextView txtCurrentTimeThread;
    private TextView txtCurrentDaysThread;
    private BatteryReceiverClass batteryReceiverClass;
    private IntentFilter intentFilter_ACTION_BATTERY_CHANGED,
            intentFilter_ACTION_STATE_CHANGED,
            intentFilter_WIFI_STATE_CHANGED_ACTION;
    private Button btnViewBattery;
    private ArrayList<ChartsModel> chartsList,getChartsList;
    private ChartsAdapter chartsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private LineDataSet lineDataSet;
    private AdView adView;
    private int hours;
    private TemplateView templateView;
    boolean landscape;
    boolean brightness;
    boolean wifi;
    ////



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_information, container, false);
        sharedPreference_utils = new SharedPreference_Utils(getActivity());
        sharedPreference_utils.getChangeLanguage(getActivity());
        chartsList =  sharedPreference_utils.getSaveBatteryCharts(getActivity(),getChartsList);
        initView();
        ///setBluetooth
        setBluetooth();
        displayCurrentTime();
//        initChart2();
        checkConnectionAds();
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_WIFI_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_STATE_CHANGED);
        getbatterySize();
        getTimeCurrent();
        return view;
    }

    private void getTimeCurrent() {
        txtCurrentDaysThread = view.findViewById(R.id.txtCurrentDaysThread);
//        Calendar calendar = Calendar.getInstance();
//        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        txtCurrentDaysThread.setText(currentDate);
//        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        String dateTime = getDateTime();
        txtCurrentDaysThread.setText(dateTime+"");
    }
    private void getbatterySize() {
        int batterySize = 0;
        batterySize= (int)  getBatteryCapacity(getActivity());
        Log.d("12213",batterySize+"");
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        Object power;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);



        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }
    private void checkConnectionAds() {
        if (Common.isConnectedtoInternet(getActivity())){
            setAdsView();
        }else {
            templateView.setVisibility(View.GONE);

        }
    }

    private void setAdsView() {
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });
        AdLoader.Builder builder = new AdLoader.Builder(getActivity(),getString(R.string.Ads_appId));
        builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (templateView!=null) {
                    templateView.setNativeAd(unifiedNativeAd);
                }
            }

        }).withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("loadADS","onAdFailedToLoad");
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
//                templateView.setVisibility(View.GONE);

                Log.d("loadADS","onAdClosed");
            }
        });
        AdLoader adLoader  = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
    }

    private void setBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
            imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        } else {
            // Bluetooth is enabled
            imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);

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

                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours+ "h"  + ":" + minutes+ "m" + ":" + seconds+ "s";
                    txtCurrentTimeThread.setText(curTime);


//                    initChart2();
                    lineDataSet.notifyDataSetChanged();
                    lineChart.notifyDataSetChanged();

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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearLayout_View:
                setActionIntent(url_Battery);
                break;
            case R.id.linearLayout_Landscape:
                landscape = !landscape;
                if (landscape){
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_rotation_24);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else {
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_lock_rotation_24);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.linearLayout_SettingWifi:
                setActionIntent(url_SettingWifi);
                break;
            case R.id.linearLayout_Bluetooth:
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter == null){
//                    Toast.makeText(getActivity(), "Your phone does not support bluetooth", Toast.LENGTH_SHORT).show();
                }else {
                    if (adapter.isEnabled()){
                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                        adapter.disable();
                    } else {
                        imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);
                        adapter.enable();
                    }
                }
                break;
            case R.id.linearLayout_Brightness:
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
                break;
            case R.id.linearLayout_WifiOnOFF:
                wifi = !wifi;
                WifiManager wifiManager;
                wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifi){
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_off_24);
//                    SharedPreference_Utils.setWifi(R.drawable.ic_baseline_signal_wifi_off_24);
                    wifiManager.setWifiEnabled(false);
                }else {
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_default);
                    wifiManager.setWifiEnabled(true);
                }
                break;
            case R.id.btnViewBattery:
                setActionIntent(url_Battery);
                break;
        }
    }

    private void initView() {
        templateView = view.findViewById(R.id.my_template);
        imageView_Bluetooth = view.findViewById(R.id.img_Bluetooth);
        imageView_Landscape = view.findViewById(R.id.img_Landscape);
        imageView_Brightness = view.findViewById(R.id.img_Brightness);
        imageView_WifiOnOff = view.findViewById(R.id.img_WifiOnOff);
        view.findViewById(R.id.btnViewBattery).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_View).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_Landscape).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_SettingWifi).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_Bluetooth).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_Brightness).setOnClickListener(this);
        view.findViewById(R.id.linearLayout_WifiOnOFF).setOnClickListener(this);
        batteryReceiverClass = new BatteryReceiverClass();
        intentFilter_ACTION_BATTERY_CHANGED = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter_ACTION_STATE_CHANGED = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter_WIFI_STATE_CHANGED_ACTION = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
    }

    private void setActionIntent(String actionIntent) {
        Intent intent = new Intent();
        intent.setAction(actionIntent);
        getActivity().startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("ActivityLifeCycler","onPause");
    }

    @Override
    public void onDestroy() {
        Log.d("ActivityLifeCycler","onDestroy");
        super.onDestroy();
        getActivity().unregisterReceiver(batteryReceiverClass);


    }
    @Override
    public void onResume() {
        super.onResume();
        checkConnectionAds();
        Log.d("ActivityLifeCycler","onResume");
//        getActivity().registerReceiver(batteryReceiverClass, intentFilter_WIFI_STATE_CHANGED_ACTION);
//        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_BATTERY_CHANGED);
//        getActivity().registerReceiver(batteryReceiverClass, intentFilter_ACTION_STATE_CHANGED);
    }

    private void initChart2() {
        lineChart  = view.findViewById(R.id.line_Charts);
        List<Entry> yValues = new ArrayList<>();
        float Y,X;
        float sum = 0;
        for (int i = 0; i< chartsList.size(); i++){
            sum++;
            Y= chartsList.get(i).getHours();
            X = chartsList.get(i).getLevelBattery();
            yValues.add(new Entry(sum,X));
        }

        for (int i = 0;i<chartsList.size();i++){
            Log.d("lisstTest","Hours: "+chartsList.get(i).getHours()+" Battery"+chartsList.get(i).getLevelBattery());
        }
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
        lineDataSet = new LineDataSet(yValues,getString(R.string.DataBattery));
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(2.5f);
        lineDataSet.setColor(Color.LTGRAY);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.WHITE);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);//
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.CYAN);
        lineDataSet.setFillAlpha(80);
        lineDataSet.notifyDataSetChanged();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data  = new LineData(dataSets);
        Drawable drawable  = ContextCompat.getDrawable(getActivity(),R.drawable.gradient_charts);
        lineDataSet.setFillDrawable(drawable);
//        lineChart.setVerticalScrollBarEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setViewPortOffsets(10,0,10,0);
        lineChart.setPinchZoom(false);
//        lineChart.setEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setData(data);
//        lineChart.setVisibleXRangeMaximum(100);
        lineChart.notifyDataSetChanged();

//        ArrayList<ChartsModel> chartsModelArrayList = chartsList;
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setValueFormatter(new MyValueFormatter(chartsModelArrayList));
//        xAxis.setGranularity(1);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//        lineChart.setVisibleYRangeMaximum(100f);
    }




    private class MyValueFormatter extends ValueFormatter implements IAxisValueFormatter{
        ArrayList<ChartsModel> chartsModels;
        private MyValueFormatter (ArrayList<ChartsModel> chartsModelArrayList){
            this.chartsModels = chartsModelArrayList;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.valueOf(chartsModels.get((int) value));
        }
    }
}
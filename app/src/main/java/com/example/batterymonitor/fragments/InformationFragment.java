package com.example.batterymonitor.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.batterymonitor.R;
import com.example.batterymonitor.receiver.BatteryReceiverClass;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

import java.util.ArrayList;
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
            linearLayout_Bluetooth,linearLayout_Brightness,linearLayout_Landscape;
    private  final String url_Battery= Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
    private final String url_SettingWifi =Settings.ACTION_WIFI_SETTINGS;
    SharedPreference_Utils sharedPreference_utils;
    private ImageView imageView_Bluetooth,imageView_Brightness,imageView_Landscape;
    private View view;
    private LineChartView lineChartView;



    private BatteryReceiverClass batteryReceiverClass;
    private IntentFilter intentFilter_ACTION_BATTERY_CHANGED,intentFilter_ACTION_STATE_CHANGED;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Log.i("ManuFacturer :", Build.MANUFACTURER);
//        Log.i("Board : ", Build.BOARD);
//        Log.i("Display : ", Build.DISPLAY);
//        Log.i("Battery : ", Build.VERSION.BASE_OS );
//        batteryTxt = (TextView) view.findViewById(R.id.batteryTxt);
//////        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        BatteryManager bm = (BatteryManager) getContext().getSystemService(BATTERY_SERVICE);
//        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        batteryTxt.setText(String.valueOf(batLevel) + "%");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_information, container, false);
        initView();
        batteryReceiverClass = new BatteryReceiverClass();

        intentFilter_ACTION_BATTERY_CHANGED = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

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
                brightness = !brightness;
                if (brightness){
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    imageView_Brightness.setImageResource(R.drawable.ic_baseline_brightness_auto_24);
                }else {
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    imageView_Brightness.setImageResource(R.drawable.ic_baseline_brightness_7_24);
                }
            }
        });
        linearLayout_Landscape.setOnClickListener(new View.OnClickListener() {
            boolean landscape;
            @Override
            public void onClick(View view) {
                landscape = !landscape;
                if (landscape){

                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_stay_current_landscape_24);
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }
                else {
                    imageView_Landscape.setImageResource(R.drawable.ic_baseline_screen_lock_landscape_24);
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }
            }
        });
        initChart();
        return view;
    }

    private void initView() {
        sharedPreference_utils = new SharedPreference_Utils(getActivity());
        lineChartView = view.findViewById(R.id.chart);
        imageView_Bluetooth = view.findViewById(R.id.img_Bluetooth);
        imageView_Landscape = view.findViewById(R.id.img_Landscape);
        imageView_Brightness = view.findViewById(R.id.img_Brightness);
        linearLayout_View =view.findViewById(R.id.linearLayout_View);
        linearLayout_Landscape = view.findViewById(R.id.linearLayout_Landscape);
        linearLayout_SettingWifi = view.findViewById(R.id.linearLayout_SettingWifi);
        linearLayout_Bluetooth = view.findViewById(R.id.linearLayout_Bluetooth);
        linearLayout_Brightness = view.findViewById(R.id.linearLayout_Brightness);
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


    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiverClass);
        super.onPause();
    }
}
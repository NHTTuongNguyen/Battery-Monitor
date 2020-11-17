package com.example.batterymonitor.receiver;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.batterymonitor.MainActivity;
import com.example.batterymonitor.activity.HomeActivity;
import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.SettingActivity;
import com.example.batterymonitor.models.ChartsModel;
import com.example.batterymonitor.service.ServiceNotifi;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.batterymonitor.activity.App.CHANNEL_ID;

public class BatteryReceiverClass extends BroadcastReceiver {
    TextView txtStatusLabel,
            txtPercentageLabel,
            txtHealth,
            txtVoltage,
            txtTemperature,
            txtLevel,
            txtBatteryType,
            txtChargingSource,
            txtPower,
            txtBigDOC,
            txtChatHeadImage;
    ImageView imgBatteryImage,imageView_Bluetooth,imageView_WifiOnOff;
    private Switch switchBluetoothCustomMode,switchNotification;
    private SharedPreference_Utils sharedPreference_utils;
    private  float percentage;
    private ArrayList<ChartsModel> chartsModels;
    int currrrTime;
    private ChartsModel chartsModel;
    private LineChart lineChart;
    private ArrayList<ChartsModel> chartsList,getChartsList;
    private LineDataSet lineDataSet;
    int seconds;
    float time;
    boolean isAdd = false;
    @Override
    public void onReceive(final Context context, Intent intent) {
        sharedPreference_utils = new SharedPreference_Utils(context);
        lineChart  = ((HomeActivity)context).findViewById(R.id.line_Charts);
        txtChatHeadImage =((HomeActivity)context).findViewById(R.id.chat_head_profile_iv);
        txtStatusLabel = ((HomeActivity)context).findViewById(R.id.txttrangthai);
        txtPercentageLabel = ((HomeActivity)context).findViewById(R.id.txtphantrampin);
        txtHealth = ((HomeActivity)context).findViewById(R.id.txtHealth);
        txtVoltage = ((HomeActivity)context).findViewById(R.id.txtVoltage);
        txtTemperature = ((HomeActivity)context).findViewById(R.id.txtTemperature);
        txtLevel = ((HomeActivity)context).findViewById(R.id.txtLevel);
        txtBatteryType = ((HomeActivity)context).findViewById(R.id.txtBatteryType);
        txtChargingSource = ((HomeActivity)context).findViewById(R.id.txtChargingSource);
        txtPower = ((HomeActivity)context).findViewById(R.id.txtPower);
        txtBigDOC = ((HomeActivity)context).findViewById(R.id.txtNhietDoLon);
        imgBatteryImage = ((HomeActivity)context).findViewById(R.id.imghinhpin);
        imageView_Bluetooth = ((HomeActivity)context).findViewById(R.id.img_Bluetooth);
        imageView_WifiOnOff = ((HomeActivity)context).findViewById(R.id.img_WifiOnOff);
        switchBluetoothCustomMode = ((HomeActivity)context).findViewById(R.id.switchBluetoothCustomMode);
        switchNotification = ((HomeActivity)context).findViewById(R.id.switchNotification);

        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            ///Status
//            ACTION_BATTERY_CHANGED
           setCharingStatus(intent,context);
            // Percentage
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percentage = level * 100 / scale;
            if (txtPercentageLabel !=null && txtLevel !=null) {
                txtPercentageLabel.setText(percentage + "%");
//                txtLevel.setText(percentage + "%");
//
                Log.d("txtPercentageLabel","run");

                Date dt = new Date();
                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();
                Log.d("minutes",minutes+"");
//                chartsList =  sharedPreference_utils.getSaveBatteryCharts(context,getChartsList);

                sharedPreference_utils.setSaveBatteryCharts(context, chartsModels, percentage, minutes);
//                initChart2(context,chartsList);
//                for (int i = 0;i<chartsList.size();i++){
//                    Log.d("getSaveBatteryCharts",chartsList.get(i).getHours()+"   "+chartsList.get(i).getLevelBattery() );
//                }
            }
            // Image
            if (imgBatteryImage!=null){
                Resources res = context.getResources();
                if (percentage >= 90) {
                    imgBatteryImage.setImageDrawable(res.getDrawable(R.drawable.b100));

                } else if (90 > percentage && percentage >= 65) {
                    imgBatteryImage.setImageDrawable(res.getDrawable(R.drawable.b75));

                } else if (65 > percentage && percentage >= 40) {
                    imgBatteryImage.setImageDrawable(res.getDrawable(R.drawable.b50));

                } else if (40 > percentage && percentage >= 15) {
                    imgBatteryImage.setImageDrawable(res.getDrawable(R.drawable.b25));

                } else {
                    imgBatteryImage.setImageDrawable(res.getDrawable(R.drawable.b0));

                }
            }
            if (txtVoltage!=null) {
                float floatVoltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) * 0.001);
                txtVoltage.setText(floatVoltage + " V");
            }
            setHealth(intent,context);
            if (txtBatteryType !=null) {
                String typeBattery = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                txtBatteryType.setText(typeBattery);
            }
            getChargingSource(intent,context);
            if (txtTemperature !=null && txtBigDOC !=null) {
                final float tempTemp = (float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10;
                txtTemperature.setText(tempTemp + " °C");
                txtBigDOC.setText(tempTemp + " °C");
            }
        }

        setChangeBluetooth(intent);
        setChangeWifi(intent);
    }


    private void initChart2(Context context,ArrayList<ChartsModel>chartsList) {
        List<Entry> yValues = new ArrayList<>();
        float Y,X;
        float sum = 0;
        for (int i = 0; i< chartsList.size(); i++){
//            sum++;
            Y= chartsList.get(i).getHours();
            X = chartsList.get(i).getLevelBattery();
            yValues.add(new Entry(Y,X));
        }

        for (int i = 0;i<chartsList.size();i++){
            Log.d("lisstTest","Hours: "+chartsList.get(i).getHours()+" Battery"+chartsList.get(i).getLevelBattery());
        }

        lineDataSet = new LineDataSet(yValues,context.getString(R.string.DataBattery));
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(2.5f);
        lineDataSet.setColor(Color.LTGRAY);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.WHITE);
        lineDataSet.setDrawValues(false);
//        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.CYAN);
        lineDataSet.setFillAlpha(80);
        lineDataSet.notifyDataSetChanged();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data  = new LineData(dataSets);
        Drawable drawable  = ContextCompat.getDrawable(context,R.drawable.gradient_charts);
        lineDataSet.setFillDrawable(drawable);
//        lineChart.setVerticalScrollBarEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(true);
//        lineChart.setViewPortOffsets(10,0,10,0);
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


    private class MyValueFormatter extends ValueFormatter implements IAxisValueFormatter {
        ArrayList<ChartsModel> chartsModels;
        private MyValueFormatter (ArrayList<ChartsModel> chartsModelArrayList){
            this.chartsModels = chartsModelArrayList;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.valueOf(chartsModels.get((int) value));
        }
    }

    private void setChangeWifi(Intent intent) {
        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                if (imageView_WifiOnOff!=null) {
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_default);
                }
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                if (imageView_WifiOnOff!=null) {
                    imageView_WifiOnOff.setImageResource(R.drawable.ic_baseline_signal_wifi_off_24);
                }
                break;
        }
    }
    private void setChangeBluetooth(Intent intent){
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
        switch(state) {
            case BluetoothAdapter.STATE_OFF:
                if (imageView_Bluetooth !=null) {
                    imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                }
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                break;
            case BluetoothAdapter.STATE_ON:
                if (imageView_Bluetooth !=null) {
                    imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);
                }
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                break;
            default:
        }
    }
    private void setCharingStatus(Intent intent,Context context) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        String message="";
        switch (status){
            case BatteryManager.BATTERY_STATUS_FULL:
                message = context.getString(R.string.Full);
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                message = context.getString(R.string.Charging);
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                message = context.getString(R.string.Discharging);
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                message = context.getString(R.string.Notcharging);
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                message = context.getString(R.string.Unknown);
                break;
        }
        if (txtStatusLabel!=null) {
            txtStatusLabel.setText(message);
        }
    }
    private void getChargingSource(Intent intent,Context context) {
        int sourceTemp = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        String message="";
        switch (sourceTemp){
            case BatteryManager.BATTERY_PLUGGED_AC:
                message = context.getString(R.string.AC);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                message = context.getString(R.string.USB);
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                message = context.getString(R.string.WIRELESS);
                break;
            default:
                message = context.getString(R.string.Unplugged);
        }
        if (txtChargingSource!=null && txtPower !=null) {
            txtChargingSource.setText(message);
            txtPower.setText(message);
        }
    }
    private void setHealth(Intent intent,Context context) {
        int val = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
        String message = "";
        switch (val){
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                message = context.getString(R.string.Unknown);
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                message = context.getString(R.string.Good);
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                message = context.getString(R.string.Overheat);
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                message = context.getString(R.string.Dead);
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                message = context.getString(R.string.OverVoltage);
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                message = context.getString(R.string.Failure);
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                message = context.getString(R.string.Cold);
                break;
        }
        if (txtHealth != null) {
            txtHealth.setText(message);
        }
    }

//

}

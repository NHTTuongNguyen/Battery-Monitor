package com.example.batterymonitor.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.activity.HomeActivity;
import com.example.batterymonitor.R;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

import java.util.Date;

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
    ImageView imgBatteryImage, imageView_BluetoothReceiver, imageView_WifiOnOffReceiver;
    private Switch switchBluetoothCustomMode,switchNotification;
    private SharedPreference_Utils sharedPreference_utils;
    private  int percentage;


    @Override
    public void onReceive(final Context context, Intent intent) {
        sharedPreference_utils = new SharedPreference_Utils(context);
        sharedPreference_utils.getChangeLanguage(context);

//        String test  = sharedPreference_utils.getChangeLanguage(context);
//        Log.d("2345678",test+"");
//        if (test != null) {
//            if (test.equals("en")) {
//                sharedPreference_utils.setChangeLanguage("en", context);
//            } else if (test.equals("vi")) {
//                sharedPreference_utils.setChangeLanguage("vi", context);
//
//            }
//        }
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
        imageView_BluetoothReceiver = ((HomeActivity)context).findViewById(R.id.img_Bluetooth);
        imageView_WifiOnOffReceiver = ((HomeActivity)context).findViewById(R.id.img_WifiOnOff);
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
                txtLevel.setText(percentage + "%");
                Date dt = new Date();
                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();
                Log.d("minutes",minutes+"");
//                sharedPreference_utils.setSaveBatteryCharts(context, chartsModels, percentage, minutes);

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
    private void setChangeWifi(Intent intent) {
        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                if (imageView_WifiOnOffReceiver !=null) {
                    imageView_WifiOnOffReceiver.setImageResource(R.drawable.ic_baseline_signal_wifi_default);
                }
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                if (imageView_WifiOnOffReceiver !=null) {
                    imageView_WifiOnOffReceiver.setImageResource(R.drawable.ic_baseline_signal_wifi_off_24);
                }
                break;
        }
    }
    private void setChangeBluetooth(Intent intent){
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
        switch(state) {
            case BluetoothAdapter.STATE_OFF:
                if (imageView_BluetoothReceiver !=null) {
                    imageView_BluetoothReceiver.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                }
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                break;
            case BluetoothAdapter.STATE_ON:
                if (imageView_BluetoothReceiver !=null) {
                    imageView_BluetoothReceiver.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);
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

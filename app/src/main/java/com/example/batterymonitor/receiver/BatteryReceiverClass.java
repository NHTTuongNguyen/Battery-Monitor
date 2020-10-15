package com.example.batterymonitor.receiver;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            txtCurrentTimeBroacat,
            txtChatHeadImage;
    ImageView imgBatteryImage,imageView_Bluetooth;
    private Switch switchBluetoothCustomMode,switchNotification;
    private SharedPreference_Utils sharedPreference_utils;
    private  int percentage;

    @Override
    public void onReceive(final Context context, Intent intent) {
        sharedPreference_utils = new SharedPreference_Utils(context);
        txtCurrentTimeBroacat = ((HomeActivity)context).findViewById(R.id.txtCurrentTimeBroacat);
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
        switchBluetoothCustomMode = ((HomeActivity)context).findViewById(R.id.switchBluetoothCustomMode);
        switchNotification = ((HomeActivity)context).findViewById(R.id.switchNotification);


        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            ///Status
           setCharingStatus(intent);
            // Percentage
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percentage = level * 100 / scale;



            if (txtPercentageLabel!=null && txtLevel!=null) {
                txtPercentageLabel.setText(percentage + "%");
                txtLevel.setText(percentage + "%");
//                sharedPreference_utils.setSaveBattery(percentage);
            }
            Date dt = new Date();
            int hours = dt.getHours();
            int minutes = dt.getMinutes();

//            String curTime = hours+ "h"  + ":" + minutes+ "m";
            int currrrTime = minutes;
            txtCurrentTimeBroacat.setText(currrrTime+"");

//            SharedPreferences sharedPreferences =context.getSharedPreferences(SharedPreference_Utils.MyPREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Gson gson = new Gson();
//            String jsonStoryWatched = sharedPreferences.getString(SharedPreference_Utils.SaveBattery, null);
//            ArrayList<ChartsModel> stringArrayList = new ArrayList<>();
//            if (jsonStoryWatched !=null){
//                Type type = new TypeToken<ArrayList<ChartsModel>>(){}.getType();/////luu mang
//                stringArrayList = gson.fromJson(jsonStoryWatched,type);
//
//                    stringArrayList.add(new ChartsModel(percentage, currrrTime + ""));
//
//            }
//            String json  =gson.toJson(stringArrayList);
//            editor.putString(SharedPreference_Utils.SaveBattery,json);
//            Log.d("set_task_list", json);
//            editor.commit();


            // [{"12:00", 90}, {13h:00, 80}]
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

            ///txtVoltage
            if (txtVoltage!=null) {
                float floatVoltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) * 0.001);
                txtVoltage.setText(floatVoltage + " V");
            }
            ////setHealth
            setHealth(intent);
            /////setBattery
            if (txtBatteryType !=null) {
                String typeBattery = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                txtBatteryType.setText(typeBattery);
            }
            /////getChargingSource
            getChargingSource(intent);

            ////Temperature
            if (txtTemperature !=null && txtBigDOC !=null) {
                final float tempTemp = (float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10;
                txtTemperature.setText(tempTemp + " °C");
                txtBigDOC.setText(tempTemp + " °C");

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent(context, ServiceNotifi.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.putExtra("message", tempTemp);
//                        Log.d("set_message",tempTemp+"");
//                        context.startService(i);
//                    }
//                }, 5000);

            }


        }

        ////BLUETOOTH
            if (action !=null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
            setChangeBluetooth(intent);

            ////wififf
                int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
                switch (wifiStateExtra){
                    case WifiManager.WIFI_STATE_ENABLED:
//                        wifiSwitch.setChecked(true);
//                        wifiSwitch.setText("WiFi is ON");
                        Log.d("wifiStateExtra","On");
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
//                        wifiSwitch.setChecked(false);
//                        wifiSwitch.setText("WiFi is OFF");
                        Log.d("wifiStateExtra","off");
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
//                        wifiSwitch.setChecked(false);
//                        wifiSwitch.setText("WiFi is OFF");
                        Log.d("wifiStateExtra","K BIET");
                        break;
                }


            }

    }
    private void setChangeBluetooth(Intent intent){
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
        switch(state) {
            case BluetoothAdapter.STATE_OFF:
                imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
//                switchBluetoothCustomMode.setChecked(false);
                Log.d("BluetoothAdapter","Bluetooth off");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Log.d("BluetoothAdapter","Turning Bluetooth off");
                break;
            case BluetoothAdapter.STATE_ON:
               imageView_Bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_24_default);
//                switchBluetoothCustomMode.setChecked(true);
                Log.d("BluetoothAdapter","Bluetooth on");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Log.d("BluetoothAdapter","Turning Bluetooth on");
                break;
            default:
                Log.d("BluetoothAdapter","sai het roi");
        }
    }
    private void setCharingStatus(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        String message="";
        switch (status){
            case BatteryManager.BATTERY_STATUS_FULL:
                message = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                Log.d("GGG","Dansaccs");
                message = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                message = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                message = "Not charging";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                message = "Unknown";
                break;
        }
        if (txtStatusLabel!=null) {
            txtStatusLabel.setText(message);
        }
    }

    private void getChargingSource(Intent intent) {
        int sourceTemp = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        String message="";
        switch (sourceTemp){
            case BatteryManager.BATTERY_PLUGGED_AC:
                message = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                message = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                message = "WIRELESS";
                break;
            default:
                message = "Unplugged";
        }
        if (txtChargingSource!=null && txtPower !=null) {
            txtChargingSource.setText(message);
            txtPower.setText(message);
        }
    }

    private void setHealth(Intent intent) {
        int val = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
        String message = "";
        switch (val){
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                message = "Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                message = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                message = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                message = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                message = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                message = "Failure";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                message = "Cold";
                break;
        }
        if (txtHealth != null) {
            txtHealth.setText(message);
        }
    }
}

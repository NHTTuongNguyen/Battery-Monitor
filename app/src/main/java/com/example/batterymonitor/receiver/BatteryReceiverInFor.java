package com.example.batterymonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batterymonitor.HomeActivity;
import com.example.batterymonitor.R;

public class BatteryReceiverInFor extends BroadcastReceiver {
    TextView txtStatusLabel,
            txtPercentageLabel,
            txtHealth,
            txtVoltage,
            txtTemperature,
            txtLevel,
            txtBatteryType,
            txtChargingSource,
            txtPower,
            txtBigDOC;
    ImageView imgBatteryImage;
    @Override
    public void onReceive(Context context, Intent intent) {
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

        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            ///Status
           setCharingStatus(intent);
            // Percentage
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percentage = level * 100 / scale;
            txtPercentageLabel.setText(percentage + "%");
            txtLevel.setText(percentage + "%");
            // Image
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

            ///txtVoltage

            float floatVoltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1)*0.001);
            txtVoltage.setText(floatVoltage+" V");

            ////setHealth
            setHealth(intent);

            /////setBattery
            txtBatteryType.setText(intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));

            /////getChargingSource
            getChargingSource(intent);

            ////Temperature
            float tempTemp = (float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1)/10;
            txtTemperature.setText(tempTemp+ " °C");
            txtBigDOC.setText(tempTemp+ " °C");




        }


    }

    private void setCharingStatus(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        String message = "";

        switch (status){
            case BatteryManager.BATTERY_STATUS_FULL:
                message = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
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
        txtStatusLabel.setText(message);
    }

    private void getChargingSource(Intent intent) {
        int sourceTemp = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        switch (sourceTemp){
            case BatteryManager.BATTERY_PLUGGED_AC:
                txtChargingSource.setText("AC");
                txtPower.setText("AC");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                txtChargingSource.setText("USB");
                txtPower.setText("USB");

                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                txtChargingSource.setText("WIRELESS");
                txtPower.setText("WIRELESS");

                break;

            default:
                txtChargingSource.setText("Unplugged");
        }
    }

    private void setHealth(Intent intent) {
        int val = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
        switch (val){
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                txtHealth.setText("Unknown");
            break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                txtHealth.setText("Good");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                txtHealth.setText("Overheat");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                txtHealth.setText("Dead");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                txtHealth.setText("Over Voltage");
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                txtHealth.setText("Failure");
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                txtHealth.setText("Cold");
                break;
        }
    }
}

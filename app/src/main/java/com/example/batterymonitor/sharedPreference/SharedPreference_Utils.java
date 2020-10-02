package com.example.batterymonitor.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.batterymonitor.R;

public class SharedPreference_Utils {
    private static  Context mcontext;
    private static SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "MyPrefsSetting77" ;
    private static final String Bluetooth_ON = "bluetooth_turn_On_Key";
    private static final String Bluetooth_OFF = "bluetooth_turn_Off_Key";


    public SharedPreference_Utils(Context context){
        mcontext=context;
        sharedPreferences  = mcontext.getSharedPreferences(MyPREFERENCES,mcontext.MODE_PRIVATE);
    }
    public static void setBluetooth_Turn_On(int bluetooth_Turn_On){
        setSharedPreferences_Int(Bluetooth_ON,bluetooth_Turn_On);
        Log.d("setBluetooth_Turn_On",bluetooth_Turn_On+"");

    }
    public static int getBluetooth_Turn_ON(){
            int selectedBluetooth = sharedPreferences.getInt(Bluetooth_ON, R.drawable.ic_baseline_bluetooth_disabled_24);
            Log.d("get_BBB", selectedBluetooth + "");
            return selectedBluetooth;
    }
    public static void setBluetooth_OFF(int bluetooth_off){
        setSharedPreferences_Int(Bluetooth_OFF,bluetooth_off);
        Log.d("setBluetooth_OFF",bluetooth_off+"");

    }
    public static int getBluetooth_Turn_Off(){
        int selectedBluetooth = sharedPreferences.getInt(Bluetooth_OFF, R.drawable.ic_baseline_bluetooth_24);
        Log.d("getBluetooth_Turn_Off", selectedBluetooth + "");

        return selectedBluetooth;
    }




    private static void setSharedPreferences_Int(String keyName, int id){
        if (sharedPreferences!=null) {
            sharedPreferences = mcontext.getSharedPreferences(MyPREFERENCES, mcontext.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(keyName, id);
            editor.apply();
        }
    }

}

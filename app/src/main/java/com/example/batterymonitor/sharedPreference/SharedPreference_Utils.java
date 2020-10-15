package com.example.batterymonitor.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.batterymonitor.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SharedPreference_Utils {
    private static  Context mcontext;
    private static SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefsSetting" ;
    private static final String Bluetooth_ON = "bluetooth_turn_On_Key";
    private static final String Bluetooth_OFF = "bluetooth_turn_Off_Key";
    private static final String Wifi= "wifi_Key";
    private static final String NightMode = "nightMode_Key";
    private static final String SwitchNotification = "switchNotification_Key";
    private static final String DesktopFloating = "desktopFloating_Key";
    private static final String RadioButtonCustomMode = "radioButtonCustomMode_Key";
    private static final String SeekBarBrightness = "seekBarBrightness_Key";
    private static final String NumberSeekBarBrightness = "numberSeekBarBrightness_Key";
    private static final String ButtonChangeBackgroundColor = "button_color_Key";
    public static final String SaveBattery = "save_Battery_Key1";
    private static final String SaveHours = "saveHours_Key1";

    public SharedPreference_Utils(Context context){
        sharedPreferences  = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
    }
    public void setSaveHours(String saveHours){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SaveHours,saveHours);
        editor.commit();
    }
    public String getSaveHours(){
        String saveHours = sharedPreferences.getString(SaveHours,null);
        Log.d("getSaveHours",saveHours);

        return saveHours;
    }
    public void setSaveBattery(int saveBattery){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SaveBattery,saveBattery);
        editor.commit();
        Log.d("set_SaveBattery_set",saveBattery+"");
    }
    public int getSaveBattery(){
        int save = sharedPreferences.getInt(SaveBattery, 0);
        Log.d("get_SaveBattery_set",save+"");
        return save;
    }

    public void setNumberSeekBarBrightness(int numberSeekBarBrightness){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NumberSeekBarBrightness,numberSeekBarBrightness);
        editor.commit();
    }
    public int getNumberSeekBarBrightness(){
        int state = sharedPreferences.getInt(NumberSeekBarBrightness,0);
        return state;
    }

    public void setSeekBarBrightness(int seekBarBrightness){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SeekBarBrightness,seekBarBrightness);
        editor.commit();
    }
    public int getSeeSeekBarBrightness(){
        int state = sharedPreferences.getInt(SeekBarBrightness,0);
        return state;
    }

    public void setSwitchNotification(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SwitchNotification,state);
        editor.commit();
    }
    public Boolean getSwitchNotification(){
        Boolean state = sharedPreferences.getBoolean(SwitchNotification,false);
        return state;
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NightMode,state);
        editor.commit();
    }
    public Boolean getNightModeState(){
        Boolean state = sharedPreferences.getBoolean(NightMode,false);
        return state;
    }

    public void setRadioButtonCustomMode(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(RadioButtonCustomMode,state);
        editor.commit();
    }
    public Boolean getRadioButtonCustomMode(){
        Boolean state = sharedPreferences.getBoolean(RadioButtonCustomMode,false);
        return state;
    }
    public void setDesktopFloating(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DesktopFloating,state);
        editor.commit();
    }
    public Boolean getDesktopFloating(){
        Boolean state = sharedPreferences.getBoolean(DesktopFloating,false);
        return state;
    }
    public void  setButtonChangeColorBackgroundSetting(int buttonColor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ButtonChangeBackgroundColor,buttonColor);
        editor.commit();
    }
    public int getButtonChangeColorBackgroundSetting(){
        int state = sharedPreferences.getInt(ButtonChangeBackgroundColor,0);
        return state;
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
    public static void setWifi(int wifi){
        setSharedPreferences_Int(Wifi,wifi);
        Log.d("setBluetooth_Turn_On",wifi+"");
    }
    public static int getWifi(){
        int selectedWifi = sharedPreferences.getInt(Wifi, R.drawable.ic_baseline_signal_wifi_4_bar_24);
        Log.d("get_BBB", selectedWifi + "");
        return selectedWifi;
    }
    public static void setBluetooth_OFF(int bluetooth_off){
        setSharedPreferences_Int(Bluetooth_OFF,bluetooth_off);
        Log.d("setBluetooth_OFF",bluetooth_off+"");

    }
    public static int getBluetooth_Turn_Off(){
        int selectedBluetooth = sharedPreferences.getInt(Bluetooth_OFF, R.drawable.ic_baseline_bluetooth_24_default);
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

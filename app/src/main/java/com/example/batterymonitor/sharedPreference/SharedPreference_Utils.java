package com.example.batterymonitor.sharedPreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.SettingActivity;
import com.example.batterymonitor.models.ChartsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
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
    public static final String ChangeLanguage = "Language_Key1";


    public SharedPreference_Utils(Context context){
        sharedPreferences  = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
    }
    public void  setChangeLanguage(String language,Context context){
        Locale locale  = new Locale(language);
        Locale.setDefault(locale);
        Configuration confi = new Configuration();
        confi.locale=locale;
        context.getResources().updateConfiguration(confi,context.getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ChangeLanguage,language);
        editor.commit();
    }
    public String getChangeLanguage(Context context){
        String saveHours = sharedPreferences.getString(ChangeLanguage,"");
        setChangeLanguage(saveHours,context);
        return saveHours;
    }
    public void setSaveBatteryCharts(Context context, ArrayList<ChartsModel>chartsModelArrayList, float percentage, float currrrTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonStoryWatched = sharedPreferences.getString(SaveBattery, null);
        chartsModelArrayList = new ArrayList<>();
        if (jsonStoryWatched !=null){
            Type type = new TypeToken<ArrayList<ChartsModel>>(){}.getType();/////luu mang
            chartsModelArrayList = gson.fromJson(jsonStoryWatched,type);
        }
        if (!hasHourInLis(chartsModelArrayList,currrrTime)){
            chartsModelArrayList.add(new ChartsModel(percentage,  currrrTime));

//            Log.d("set_chartArrayList",percentage+"  "+ currrrTime);

        }
//        chartsModelArrayList.add(new ChartsModel(percentage,  currrrTime));
        String json  =gson.toJson(chartsModelArrayList);
        editor.putString(SharedPreference_Utils.SaveBattery,json);
        Log.d("set_chartArrayList",json);
        editor.commit();
    }
    public ArrayList<ChartsModel> getSaveBatteryCharts(Context context,ArrayList<ChartsModel> chartsList) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SaveBattery, null);
        Type type = new TypeToken<ArrayList<ChartsModel>>(){}.getType();
        chartsList = gson.fromJson(json, type);
        Log.d("get_chartsArrayList",String.valueOf(json));
        if (chartsList == null) {
            chartsList = new ArrayList<>();
        }
        return chartsList;
    }
    public void removeSaveBatteryThan24h(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SaveBattery);
        editor.commit();
    }
    private boolean     hasHourInLis(ArrayList<ChartsModel> chartsModelArrayList, float idHour){
        for (int i = 0;i<chartsModelArrayList.size();i++){
            if (chartsModelArrayList.get(i).getHours() == idHour){
                return true;
            }
        }
        return false;
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
    public void setButtonFocusCustomMode(int buttonColor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ButtonChangeBackgroundColor,buttonColor);
        editor.commit();
        Log.d("ButtonFous",buttonColor+"");
    }
    public int getButtonFocusCustomMode(){
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

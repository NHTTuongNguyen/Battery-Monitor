package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.service.ServiceNotifi;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TestActivity extends AppCompatActivity{
    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";
    private TextView textView;
    private Button button;
    private EditText editText;
    String edtname;
    private SeekBar seekBarTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        textView = (TextView) findViewById(R.id.myTemp);

        seekBarTest = findViewById(R.id.seekBarTest);
        seekBarTest.setMin(15000);
        seekBarTest.setMax(1800000);
        seekBarTest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarProgress = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setScreenTimeout(seekBarProgress);
                textView.setText(seekBarProgress+"");
            }
        });





    }
    private void setScreenTimeout(int millisecounds) {
        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, millisecounds);
    }
    private void setScreen_Brightness(int screenBrightness) {
        android.provider.Settings.System.putInt(
               getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, screenBrightness);
    }
    private String ReadCPUinfo()
    {
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
}
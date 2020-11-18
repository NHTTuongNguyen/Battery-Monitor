package com.example.batterymonitor.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.Utils.SizeNumber;
import com.example.batterymonitor.service.ServiceNotifi;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TestActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    SharedPreference_Utils sharedPreference_utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sharedPreference_utils = new SharedPreference_Utils(this);
        radioGroup = findViewById(R.id.radioGroup);
        int checkId = radioGroup.getCheckedRadioButtonId();
        findViewGroupButton(checkId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               findViewGroupButton(i);
                radioButton = findViewById(i);
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
                sharedPreference_utils.setChangeButtonRadioLanguage(checkedIndex);
                Log.d("BBBB",checkedIndex+"");




            }
        });
        sharedPreference_utils.getChangeButtonRadioLanguage(radioGroup);
    }

    private void findViewGroupButton(int checkId) {
        switch (checkId){
            case R.id.radioButtonEnglish:
                Toast.makeText(this, "en", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonVietnam:
                Toast.makeText(this, "vi", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
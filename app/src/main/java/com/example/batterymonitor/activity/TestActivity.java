package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
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
    private Button[] btn_position_change_background = new Button[3];
    private Button button_change_background;
    private int[] btn_id_change_background = {
            R.id.btntrang,
            R.id.btnden,
            R.id.btnvang};
    private Button btntrang,btnden,btnvang;

    private SharedPreference_Utils sharedPreference_utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sharedPreference_utils = new SharedPreference_Utils(this);
        btntrang = findViewById(R.id.btntrang);
        btnden =findViewById(R.id.btnden);
        btnvang =findViewById(R.id.btnvang);


        initGroupButtonChangeBackground();
        if (sharedPreference_utils !=null) {
            getIdButtonChangeBackgroundClick();
        }

        btntrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestActivity.this, "1", Toast.LENGTH_SHORT).show();
                setFocusToGroupButtonChangeBackground(button_change_background, btn_position_change_background[0]);
            }
        });
        btnden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestActivity.this, "2", Toast.LENGTH_SHORT).show();

                setFocusToGroupButtonChangeBackground(button_change_background, btn_position_change_background[1]);
            }
        });
        btnvang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestActivity.this, "3", Toast.LENGTH_SHORT).show();

                setFocusToGroupButtonChangeBackground(button_change_background, btn_position_change_background[2]);
            }
        });


//

    }

    private void initGroupButtonChangeBackground() {
        for(int i = 0; i < btn_position_change_background.length; i++){
            btn_position_change_background[i] = (Button) findViewById(btn_id_change_background[i]);
//            btn_position_change_background[i].setOnClickListener(this);
        }
        button_change_background = btn_position_change_background[0];
    }
    private void getIdButtonChangeBackgroundClick() {
        int idButton = sharedPreference_utils.getButtonFocusCustomMode();
        if (idButton  != 0){
            Button btnColorF = (Button) findViewById(idButton);
            setFocusToGroupButtonChangeBackground(button_change_background,btnColorF);
        }
    }

    private void setFocusToGroupButtonChangeBackground(Button btn_unfocus, Button btn_focus){
        setFocus(btn_unfocus,btn_focus);
//        SettingSharedPreferences.setButtonChangeColorBackgroundSetting(btn_focus.getId());
        this.button_change_background = btn_focus;
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setBackgroundResource(R.drawable.rounded_corners_blue_white);
        btn_unfocus.setTextColor(getResources().getColor(R.color.colorBlack));
        btn_focus.setBackgroundResource(R.drawable.rounded_corners_blue);
        btn_focus.setTextColor(getResources().getColor(R.color.colorWhite));

    }

}
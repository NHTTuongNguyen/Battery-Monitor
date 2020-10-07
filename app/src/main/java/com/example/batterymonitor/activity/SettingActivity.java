package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.batterymonitor.R;
import com.example.batterymonitor.service.ExampleService;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;


public class SettingActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout_Rate,relativeLayout_MoreApp;
    private ImageView imgSettingCancel;
    private final String URL_Rate = "https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi";
    private final String URL_MORE_APP = "https://play.google.com/store/apps/dev?id=9089535463678444622&hl=vi";
    private Switch switchNotification;
    private Switch switchChangeDarkMode,switchDesktopMode;
    private SharedPreference_Utils sharePre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePre = new SharedPreference_Utils(SettingActivity.this);
        if (sharePre.getNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        relativeLayout_Rate = findViewById(R.id.relativeLayout_Rate);
        relativeLayout_MoreApp = findViewById(R.id.relativeLayout_MoreApp);
        switchChangeDarkMode = findViewById(R.id.switchChangeDarkMode);
        imgSettingCancel = findViewById(R.id.imgSettingCancel);
        switchNotification = findViewById(R.id.switchNotification);
        switchDesktopMode = findViewById(R.id.switchDesktopMode);
        imgSettingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        relativeLayout_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent(URL_Rate);
            }
        });
        relativeLayout_MoreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent(URL_MORE_APP);

            }
        });
        setEventSwitchNotification();
        setEventSwitchDesktopMode();
        setEventSwitchChangeDarkMode();

    }

    private void setEventSwitchDesktopMode() {
        if (sharePre.getDesktopFloating() == true){
            switchDesktopMode.setChecked(true);
        }
        switchDesktopMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharePre.setDesktopFloating(true);
                }else {
                    sharePre.setDesktopFloating(false);
                }
            }
        });
    }

    private void setEventSwitchChangeDarkMode() {
        if (sharePre.getNightModeState() == true){
            switchChangeDarkMode.setChecked(true);
        }
        switchChangeDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharePre.setNightModeState(true);
                    restartApp();
                }else {
                    sharePre.setNightModeState(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });
    }
    private void setEventSwitchNotification() {
        if (sharePre.getSwitchDarkMode() == true){
            switchNotification.setChecked(true);
        }
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharePre.setSwitchDarkMode(true);
                    Intent serviceIntent = new Intent(getApplicationContext(), ExampleService.class);
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                }else {
                    sharePre.setSwitchDarkMode(false);
                    Intent serviceIntent = new Intent(getApplicationContext(), ExampleService.class);
                    stopService(serviceIntent);
                }
            }
        });
    }
    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void setActionIntent(String actionIntent) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(actionIntent));
        startActivity(i);
    }
}
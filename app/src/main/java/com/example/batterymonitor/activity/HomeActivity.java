package com.example.batterymonitor.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.adapter.ViewPagerAdapter;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {
    private String tabTitles[] = new String[] { "Information", "Saver"};
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView imgToSetting;
    private SharedPreference_Utils sharedPreference_utils;
    @Override
    protected void onResume() {
//        sharedPreference_utils = new SharedPreference_Utils(this);
//        if (sharedPreference_utils.getNightModeState() == true){
//            setTheme(R.style.DarkTheme);
//        }else {
//            setTheme(R.style.AppTheme);
//        }
        super.onResume();
        Log.d("ASD","onResume");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreference_utils = new SharedPreference_Utils(this);

        if (sharedPreference_utils.getNightModeState() == true){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        imgToSetting = findViewById(R.id.imgToSetting);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        // Setup Tab Layout with View Pager 2
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                    }
                }).attach();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i, HomeActivity.this));
        }
        // Add event to custom UI
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                adapter.SetOnSelectView(tabLayout, p, HomeActivity.this);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                adapter.SetUnSelectView(tabLayout, p, HomeActivity.this);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imgToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });



    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                            }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

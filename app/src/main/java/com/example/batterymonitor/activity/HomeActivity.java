package com.example.batterymonitor.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.batterymonitor.R;
import com.example.batterymonitor.adapter.ViewPagerAdapter;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
//    private String tabTitles[] = new String[] {this.getResources().getString(R.string.Information),this.getResources().getString(R.string.Saver)};
    //private String tabTitles[] = new String[] {"Information","Saver"};
//    private String tabTitles [];
    ArrayList<String> tabTitles;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView imgToSetting;
    private SharedPreference_Utils  sharedPreference_utils;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreference_utils = new SharedPreference_Utils(this);
        sharedPreference_utils.getChangeLanguage(HomeActivity.this);
        if (sharedPreference_utils.getNightModeState() == true){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        floatingActionButton = findViewById(R.id.floatNoti);
        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        imgToSetting = findViewById(R.id.imgToSetting);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        tabTitles = new ArrayList<>();
        tabTitles.add(getString(R.string.Information));
        tabTitles.add(getString(R.string.Saver));
        // Setup Tab Layout with View Pager 2
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        tab.setText(tabTitles[position]);

                        tab.setText(tabTitles.get(position));

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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDialogNotification();
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        });

//       String oo = Locale.getDefault().getLanguage();
//        String locale = getResources().getConfiguration().locale.getLanguage();
//        if (oo==null){
//            sharedPreference_utils.setChangeLanguage(oo,HomeActivity.this);
//        }
//        Log.d("GGGG",oo+"");
//        Log.d("AAAAAAA","HELLO");


//        String keySaveShare = sharedPreference_utils.getChangeLanguage(this);
//        sharedPreference_utils.setChangeLanguage(languageSystem,HomeActivity.this);
//        Log.d("languageSystem",languageSystem+"");
//        Log.d("keySaveShare",keySaveShare+"");
//        if (keySaveShare!=null) {
//            if (languageSystem.contains(keySaveShare) == false) {
//                sharedPreference_utils.removeChangeButtonRadioLanguage();
//            }
//        }
//        sharedPreference_utils.setChangeLanguage(languageSystem,this);
//        if (!languageSystem.equals(sharedPreference_utils.getChangeLanguage(this))){
//            sharedPreference_utils.removeChangeButtonRadioLanguage();
//        }
//        sharedPreference_utils.setChangeLanguage(languageSystem,this);


    }
    private void showDialogNotification() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.dialong_bottom,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.button_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK),
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
//    private String getLanguage (){
//        SharedPreferences sharedPreferences  = getSharedPreferences(SharedPreference_Utils.MyPREFERENCES, Context.MODE_PRIVATE);
//        String getChangeLanguage = sharedPreferences.getString(SharedPreference_Utils.ChangeLanguage,"");
//        ((SettingActivity)).setLocale(getChangeLanguage);
//        return getChangeLanguage;
//
//    }
}

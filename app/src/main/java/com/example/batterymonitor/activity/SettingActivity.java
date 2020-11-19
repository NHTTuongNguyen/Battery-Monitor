package com.example.batterymonitor.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.batterymonitor.Common.Common;
import com.example.batterymonitor.R;
import com.example.batterymonitor.service.ServiceNotifi;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class SettingActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout_Rate,relativeLayout_MoreApp,relativeLayout_ChoiceLanguage;
    private ImageView imgSettingCancel;
    private final String URL_Rate = "https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi";
    private final String URL_MORE_APP = "https://play.google.com/store/apps/dev?id=9089535463678444622&hl=vi";
    private Switch switchNotification;
    private Switch switchChangeDarkMode,switchDesktopMode;
    private SharedPreference_Utils sharedPreference_utils;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private TemplateView templateView;
    private RadioGroup radioGroup;
    RadioButton radioButton;
    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreference_utils = new SharedPreference_Utils(SettingActivity.this);
        sharedPreference_utils.getChangeLanguage(SettingActivity.this);
        if (sharedPreference_utils.getNightModeState() == true){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        imgSettingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        relativeLayout_ChoiceLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDialogChangeLanguge();
                showDialogChangeLanguage();
            }
        });
        setEventSwitchNotification();
        setEventSwitchChangeDarkMode();
        checkConnectionAds();


    }

    private void showDialogChangeLanguage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ChangeLanguage));
        LayoutInflater inflater = this.getLayoutInflater();
        View viewLayout = inflater.inflate(R.layout.dialog_changelanguage,null);
        ////init
        radioGroup = viewLayout.findViewById(R.id.radioGroup);
        sharedPreference_utils.getChangeButtonRadioLanguage(radioGroup);
        ///// lưu radiobutton đã nhấn
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                findViewGroupButton(i);
                radioButton = findViewById(i);
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
                sharedPreference_utils.setChangeButtonRadioLanguage(checkedIndex);
            }
        });
        builder.setView(viewLayout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void findViewGroupButton(int checkId) {
        switch (checkId){
            case R.id.radioButtonEnglish:
                sharedPreference_utils.setChangeLanguage("en",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonVietnam:
                sharedPreference_utils.setChangeLanguage("vi",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonSpanish:
                sharedPreference_utils.setChangeLanguage("es",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonPortuguese:
                sharedPreference_utils.setChangeLanguage("pt",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonFrench:
                sharedPreference_utils.setChangeLanguage("fr",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonRussian:
                sharedPreference_utils.setChangeLanguage("ru",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonGerman:
                sharedPreference_utils.setChangeLanguage("de",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonKorean:
                sharedPreference_utils.setChangeLanguage("ko",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonArabic:
                sharedPreference_utils.setChangeLanguage("ar",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonJapanese:
                sharedPreference_utils.setChangeLanguage("ja",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
            case R.id.radioButtonItalian:
                sharedPreference_utils.setChangeLanguage("it",SettingActivity.this);
                recreate();
                alertDialog.dismiss();
                break;
        }
    }
    private void checkConnectionAds() {
        if (Common.isConnectedtoInternet(this)){
            setAdsView();
        }else {
            templateView.setVisibility(View.GONE);
        }
    }
    private void setAdsView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });
        AdLoader.Builder builder = new AdLoader.Builder(this,getString(R.string.Ads_appId));
        builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        if (templateView!=null) {
                            templateView.setNativeAd(unifiedNativeAd);
                        }
                    }

                }).withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("loadADS","onAdFailedToLoad");
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
//                templateView.setVisibility(View.GONE);

                Log.d("loadADS","onAdClosed");
            }
        });
        AdLoader adLoader  = builder.build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
    }
    private void initView() {
        relativeLayout_Rate = findViewById(R.id.relativeLayout_Rate);
        relativeLayout_ChoiceLanguage = findViewById(R.id.relativeLayout_ChoiceLanguage);
        relativeLayout_MoreApp = findViewById(R.id.relativeLayout_MoreApp);
        switchChangeDarkMode = findViewById(R.id.switchChangeDarkMode);
        imgSettingCancel = findViewById(R.id.imgSettingCancel);
        switchNotification = findViewById(R.id.switchNotification);
        switchDesktopMode = findViewById(R.id.switchDesktopMode);
        templateView = findViewById(R.id.my_template_Setting);
    }
    private void setEventSwitchChangeDarkMode() {
        if (sharedPreference_utils.getNightModeState() == true){
            switchChangeDarkMode.setChecked(true);
        }
        switchChangeDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharedPreference_utils.setNightModeState(true);
                    restartApp();
                }else {
                    sharedPreference_utils.setNightModeState(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });
    }
    private void setEventSwitchNotification() {
        if (sharedPreference_utils.getSwitchNotification() == true){
            switchNotification.setChecked(true);
        }
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharedPreference_utils.setSwitchNotification(true);
//                    Intent serviceIntent = new Intent(getApplicationContext(), ServiceNotifi.class);
//                    serviceIntent.putExtra("inputExtra",tess);
//                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
//                    startService(serviceIntent);
                }else {
//                    switchDesktopMode.setChecked(false);
                    sharedPreference_utils.setSwitchNotification(false);
//                    Intent serviceIntent = new Intent(getApplicationContext(), ServiceNotifi.class);
//                    stopService(serviceIntent);
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
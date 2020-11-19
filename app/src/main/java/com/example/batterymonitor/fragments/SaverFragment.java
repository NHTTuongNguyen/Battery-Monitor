package com.example.batterymonitor.fragments;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.Utils.SizeNumber;
import com.example.batterymonitor.dialog.TimePickerFragment;
import com.example.batterymonitor.receiver.BatteryReceiverClass;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SaverFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SaverFragment newInstance(String param1, String param2) {
        SaverFragment fragment = new SaverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View viewClassicModel,viewLongLifeMode,viewSleepModel,viewCustomMode;
    private BatteryReceiverClass batteryReceiverClass;
    private IntentFilter intentFilter_ACTION_BATTERY_CHANGED,
            intentFilter_ACTION_STATE_CHANGED,
            intentFilter_WIFI_STATE_CHANGED_ACTION;
    private RadioButton radioButtonClassicMode,
            radioButtonLongLifeMode,
            radioButtonSleepMode,
            radioButtonCustomMode;
    private TextView txtClassicMode,
            txtLongLifeMode,
            txtSleepMode,
            txtCustomMode;
    private View view;
    private ImageView imgClassicMode,
            imgLongLifeMode,
            imgSleepMode,
            imgCusTomMode;
    private LinearLayout linearLayoutClassMode_Line,
            linearLayoutLongLifeMode_Line,
            linearLayoutSleepMode_Line,
            linearLayoutCustomMode_Line;
    private LinearLayout linearLayoutClassMode,
            linearLayoutLongLifeMode,
            linearLayoutSleepMode,
            linearLayoutCustomMode;
    private Button btnStartSleepMode,btnStopSleepMode;
    private Button btnScreenTimeoutCustomMode,btnBrightnessCustomMode;
    private Button btn15s,btn30s,btn1m,btn5m,btn10m,btn15m,btn20m,btn30m;
    private Switch switchVolumeCustomMode,switchBluetoothCustomMode,switchWifiCustomMode;
    private WifiManager wifiManager;
    private SharedPreference_Utils sharedPreference_utils;
    private AlertDialog alertDialog;

    Button[] btn = new Button[8];
    Button btn_unfocus;
    int[] btn_id = {
            R.id.btn15s,
            R.id.btn30s,
            R.id.btn1m,
            R.id.btn5m,
            R.id.btn10m,
            R.id.btn15m,
            R.id.btn20m,
            R.id.btn30m};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        sharedPreference_utils = new SharedPreference_Utils(getActivity());
//        sharedPreference_utils.getChangeLanguage(getActivity());

        batteryReceiverClass = new BatteryReceiverClass();
        intentFilter_ACTION_BATTERY_CHANGED = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter_ACTION_STATE_CHANGED = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter_WIFI_STATE_CHANGED_ACTION = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);

        view = inflater.inflate(R.layout.fragment_saver, container, false);

       initView();



        radioButtonClassicMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdClassicMode) {
                setChoiceRadioButtonClassicMode(checkIdClassicMode);
            }
        });
        radioButtonLongLifeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdLongLifeMode) {
                setChoiceRadioButtonLongLifeMode(checkIdLongLifeMode);
            }
        });
        radioButtonSleepMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdSleepMode) {
                setChoiceRadioButtonSleepMode(checkIdSleepMode);
            }
        });
        radioButtonCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdCustomMode) {
                setChoiceRadioButtonCustomMode(checkIdCustomMode);
            }
        });
        setVisibilityLinearLayout();
///////////////////////////////////////////////////////////
        eventBtnStartSleepMode();
        eventBtnStopMode();
///////////////////////////////////////////////////////
        return view;
    }

    private void initView() {
        viewClassicModel = view.findViewById(R.id.viewClassicModel);
        viewLongLifeMode = view.findViewById(R.id.viewLongLifeMode);
        viewSleepModel = view.findViewById(R.id.viewSleepModel);
        viewCustomMode = view.findViewById(R.id.viewCustomMode);

        btnBrightnessCustomMode = view.findViewById(R.id.btnBrightnessCustomMode);
        btnScreenTimeoutCustomMode = view.findViewById(R.id.btnScreenTimeoutCustomMode);


        switchVolumeCustomMode = view.findViewById(R.id.switchVolumeCustomMode);
        switchBluetoothCustomMode = view.findViewById(R.id.switchBluetoothCustomMode);
        switchWifiCustomMode = view.findViewById(R.id.switchWifiCustomMode);

        linearLayoutClassMode_Line = view.findViewById(R.id.linearLayoutClassMode_Line);
        linearLayoutLongLifeMode_Line = view.findViewById(R.id.linearLayoutLongLifeMode_Line);
        linearLayoutSleepMode_Line = view.findViewById(R.id.linearLayoutSleepMode_Line);
        linearLayoutCustomMode_Line = view.findViewById(R.id.linearLayoutCustomMode_Line);

        linearLayoutClassMode = view.findViewById(R.id.linearLayoutClassMode);
        linearLayoutLongLifeMode = view.findViewById(R.id.linearLayoutLongLifeMode);
        linearLayoutSleepMode = view.findViewById(R.id.linearLayoutSleepMode);
        linearLayoutCustomMode = view.findViewById(R.id.linearLayoutCustomMode);

        txtClassicMode = view.findViewById(R.id.txtClassicMode);
        txtLongLifeMode = view.findViewById(R.id.txtLongLifeMode);
        txtSleepMode = view.findViewById(R.id.txtSleepMode);
        txtCustomMode = view.findViewById(R.id.txtCustomMode);

        imgClassicMode = view.findViewById(R.id.img_ClassicMode);
        imgLongLifeMode = view.findViewById(R.id.img_LongLifeMode);
        imgSleepMode = view.findViewById(R.id.imgSleepMode);
        imgCusTomMode = view.findViewById(R.id.imgCusTomMode);

        radioButtonClassicMode = view.findViewById(R.id.radioButtonClassicMode);
        radioButtonLongLifeMode = view.findViewById(R.id.radioButtonLongLifeMode);
        radioButtonSleepMode = view.findViewById(R.id.radioButtonSleepMode);
        radioButtonCustomMode = view.findViewById(R.id.radioButtonCustomMode);

        btnStartSleepMode  =view.findViewById(R.id.btnStartSleepMode);
        btnStopSleepMode = view.findViewById(R.id.btnStopSleepMode);
    }

    private void setChoiceRadioButtonClassicMode(Boolean checkId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(getActivity())){
                if (checkId) {
                    setEventRadioButtonClassicMode();
                }
            }
            else {
                radioButtonClassicMode.setChecked(false);
                alertDialogPermission_WriteSettings();
            }
        }else {
            if (checkId){
                setEventRadioButtonClassicMode();
            }
        }
    }
    private void setChoiceRadioButtonLongLifeMode(Boolean checkIdLongLifeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(getActivity())){
                if (checkIdLongLifeMode) {
                    setEventRadioButtonLongLifeMode();
                }
            }else {
                radioButtonLongLifeMode.setChecked(false);
                alertDialogPermission_WriteSettings();
            }
        }else {
            if (checkIdLongLifeMode){
                setEventRadioButtonLongLifeMode();
            }
        }
    }
    private void setChoiceRadioButtonSleepMode(Boolean checkIdSleepMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(getActivity())){
                if (checkIdSleepMode) {
                    setEventRadioButtonSleepMode();

                }
            }else {
                radioButtonSleepMode.setChecked(false);
                alertDialogPermission_WriteSettings();
            }

        }else {
            if (checkIdSleepMode){
                setEventRadioButtonSleepMode();
            }
        }

    }
    private void setChoiceRadioButtonCustomMode(Boolean checkIdCustomMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(getActivity())){
                if (checkIdCustomMode) {
                    setEventRadioButtonCustomMode();
                }
            }else {
                radioButtonCustomMode.setChecked(false);
                alertDialogPermission_WriteSettings();
            }
        }else {
            if (checkIdCustomMode){
                setEventRadioButtonCustomMode();
            }
        }
    }
    private void setEventRadioButtonClassicMode() {
        radioButtonLongLifeMode.setChecked(false);
        radioButtonSleepMode.setChecked(false);
        radioButtonCustomMode.setChecked(false);
        ///setVisibility
        viewClassicModel.setVisibility(View.GONE);
        viewLongLifeMode.setVisibility(View.VISIBLE);
        viewSleepModel.setVisibility(View.VISIBLE);
        viewCustomMode.setVisibility(View.VISIBLE);

        linearLayoutClassMode_Line.setVisibility(View.VISIBLE);
        linearLayoutClassMode.setVisibility(View.VISIBLE);

        linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);
        linearLayoutLongLifeMode.setVisibility(View.GONE);

        linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);
        linearLayoutSleepMode.setVisibility(View.GONE);

        linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
        linearLayoutCustomMode.setVisibility(View.GONE);


        ////setColorTextView
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
        txtClassicMode.setTextColor(getResources().getColor(R.color.colorGreen));
        txtLongLifeMode.setTextColor(color);
        txtSleepMode.setTextColor(color);
        txtCustomMode.setTextColor(color);
        ////setColorImageView
        imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24_color_green);
        imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
        imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24);

        //////

        ///setScreenTimeout
        setScreenTimeout(SizeNumber.Thirty_seconds);
        ////SCREEN_BRIGHTNESS
        setScreen_Brightness(SizeNumber.namnoiphantrampercent);
        ////setAudioManager
        setVolumeTurnOff();
        ///setBluetoothDisable
        setBluetoothTurnOff();
        /////
        setWifiTurnOn();
    }
    private void setEventRadioButtonLongLifeMode() {
        radioButtonClassicMode.setChecked(false);
        radioButtonSleepMode.setChecked(false);
        radioButtonCustomMode.setChecked(false);
        viewClassicModel.setVisibility(View.VISIBLE);
        viewLongLifeMode.setVisibility(View.GONE);
        viewSleepModel.setVisibility(View.VISIBLE);
        viewCustomMode.setVisibility(View.VISIBLE);
        linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);
        linearLayoutLongLifeMode.setVisibility(View.VISIBLE);

        linearLayoutClassMode_Line.setVisibility(View.VISIBLE);
        linearLayoutClassMode.setVisibility(View.GONE);

        linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);
        linearLayoutSleepMode.setVisibility(View.GONE);

        linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
        linearLayoutCustomMode.setVisibility(View.GONE);
        ////setColorTextView
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
        txtLongLifeMode.setTextColor(getResources().getColor(R.color.colorGreen));
        txtClassicMode.setTextColor(color);
        txtSleepMode.setTextColor(color);
        txtCustomMode.setTextColor(color);
        ////setColorImageView
        imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24_default);
        imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24_color_green);
        imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
        imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24);
        ///setVisibility

        ///setScreenTimeout
        setScreenTimeout(SizeNumber.Fifteen_seconds);
        ///setScreen_Brightness
        setScreen_Brightness(SizeNumber.haimuoiphantrampercent);
        ////setAudioManager
        setVolumeTurnOff();
        ///setBluetoothDisable
        setBluetoothTurnOff();
        /////setWifiOff
        setWifiTurnOff();

//        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if(wifiManager.isWifiEnabled()){
//            wifiManager.setWifiEnabled(false);
//        }else{
//            wifiManager.setWifiEnabled(true);
//        }
    }
    private void setEventRadioButtonSleepMode() {
        radioButtonClassicMode.setChecked(false);
        radioButtonLongLifeMode.setChecked(false);
        radioButtonCustomMode.setChecked(false);
        viewClassicModel.setVisibility(View.VISIBLE);
        viewLongLifeMode.setVisibility(View.VISIBLE);
        viewSleepModel.setVisibility(View.GONE);
        viewCustomMode.setVisibility(View.VISIBLE);
        linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);
        linearLayoutSleepMode.setVisibility(View.VISIBLE);
        linearLayoutClassMode_Line.setVisibility(View.VISIBLE);
        linearLayoutClassMode.setVisibility(View.GONE);
        linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);
        linearLayoutLongLifeMode.setVisibility(View.GONE);
        linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
        linearLayoutCustomMode.setVisibility(View.GONE);
        ////setColorTextView
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
        txtSleepMode.setTextColor(getResources().getColor(R.color.colorGreen));
        txtClassicMode.setTextColor(color);
        txtLongLifeMode.setTextColor(color);
        txtCustomMode.setTextColor(color);
        ////setColorImageView
        imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24_default);
        imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24_color_green);
        imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24);
        ///setVisibility

        setScreenTimeout(SizeNumber.Fifteen_seconds);
        ///setScreen_Brightness
        setScreen_Brightness(SizeNumber.haimuoiphantrampercent);
        ////setAudioManager
        setVolumeTurnOff();
        ///setBluetoothDisable
        setBluetoothTurnOff();
        setWifiTurnOff();
    }
    private void setEventRadioButtonCustomMode() {
        radioButtonClassicMode.setChecked(false);
        radioButtonLongLifeMode.setChecked(false);
        radioButtonSleepMode.setChecked(false);
        ///setVisibility
        viewClassicModel.setVisibility(View.VISIBLE);
        viewLongLifeMode.setVisibility(View.VISIBLE);
        viewSleepModel.setVisibility(View.VISIBLE);
        viewCustomMode.setVisibility(View.GONE);

        linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
        linearLayoutCustomMode.setVisibility(View.VISIBLE);

        linearLayoutClassMode_Line.setVisibility(View.VISIBLE);
        linearLayoutClassMode.setVisibility(View.GONE);

        linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);
        linearLayoutLongLifeMode.setVisibility(View.GONE);

        linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);
        linearLayoutSleepMode.setVisibility(View.GONE);

        ////setColorTextView
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
        txtCustomMode.setTextColor(getResources().getColor(R.color.colorGreen));
        txtClassicMode.setTextColor(color);
        txtLongLifeMode.setTextColor(color);
        txtSleepMode.setTextColor(color);
        ////setColorImageView
        imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24_default);
        imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
        imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24_color_green);
        ///setVisibility

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        switchWifiCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    wifiManager.setWifiEnabled(true);
                }else {
                    wifiManager.setWifiEnabled(false);
                }
            }
        });
        ///setSwitchVolumeCustomMode
        switchVolumeCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    NotificationManager notificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
                        Intent intent = new Intent(android.provider.Settings
                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                    }
                    setVolumeTurnOn();
                }else {
                    setVolumeTurnOff();
                }
            }
        });
        ////setBluetooth
        switchBluetoothCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter == null){
                    Toast.makeText(getActivity(), "Your phone does not support bluetooth", Toast.LENGTH_SHORT).show();
                }else {
                    if (b){
                        adapter.enable();
                    }else {
                        adapter.disable();
                    }
                }
            }
        });
        ///setButtonBrightnessCustomMode;
        btnBrightnessCustomMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBrightnessCustomMode();

            }
        });
        btnScreenTimeoutCustomMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogScreenTimeoutCustomMode();

            }
        });
        ///wwifi
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

//                    switchWifiCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                            if (b){
//                                wifiManager.setWifiEnabled(true);
//                            }else {
//                                wifiManager.setWifiEnabled(false);
//                            }
//                        }
//                    });
//                    if (wifiManager.isWifiEnabled()){
//                        switchWifiCustomMode.setChecked(true);
//                    }else {
//                        switchWifiCustomMode.setChecked(false);
//                    }
    }
    private void setWifiTurnOn() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
    private void setWifiTurnOff() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }
    private void showDialogScreenTimeoutCustomMode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.ScreenTimeOut));
////      builder.setMessage(getString(R.string.ChangeBrightness));
//        builder.setIcon(R.drawable.ic_baseline_brightness_1_24_green);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewLayout = inflater.inflate(R.layout.dialog_screentimeout,null);
        btn15s = viewLayout.findViewById(R.id.btn15s);
        btn30s = viewLayout.findViewById(R.id.btn30s);
        btn1m = viewLayout.findViewById(R.id.btn1m);
        btn5m = viewLayout.findViewById(R.id.btn5m);
        btn10m = viewLayout.findViewById(R.id.btn10m);
        btn15m = viewLayout.findViewById(R.id.btn15m);
        btn20m = viewLayout.findViewById(R.id.btn20m);
        btn30m = viewLayout.findViewById(R.id.btn30m);
//
        builder.setView(viewLayout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
        ////setFocus
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) viewLayout.findViewById(btn_id[i]);
        }
        btn_unfocus = btn[0];
        ///
        int idButton = 0;
        getFocusSharedPreCustomModel(idButton,viewLayout,btn_unfocus);

        ///setEvent
        btn15s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[0]);
                setScreenTimeout(SizeNumber.Fifteen_seconds);
                alertDialog.dismiss();
            }
        });
        btn30s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Thirty_seconds);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[1]);
//                alertDialog.dismiss();

            }
        });
        btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.One_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[2]);
//                alertDialog.dismiss();

            }
        });
        btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Five_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[3]);
//                alertDialog.dismiss();

            }
        });
        btn10m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Ten_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[4]);
//                alertDialog.dismiss();

            }
        });
        btn15m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Fifteen_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[5]);
//                alertDialog.dismiss();

            }
        });
        btn20m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Twenty_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[6]);
//                alertDialog.dismiss();

            }
        });
        btn30m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScreenTimeout(SizeNumber.Thirty_minute);
                setFocusToGroupButtonChangeBackground(btn_unfocus, btn[7]);
//                alertDialog.dismiss();

            }
        });




    }
    private void getFocusSharedPreCustomModel(int idButton, View viewLayout, Button btn_unfocus ) {
        idButton = sharedPreference_utils.getButtonFocusCustomMode();
        if (idButton  != 0){
            Button btnColorF = (Button)viewLayout.findViewById(idButton);
            setFocusToGroupButtonChangeBackground(btn_unfocus,btnColorF);
        }
    }
    private void setFocusToGroupButtonChangeBackground(Button btn_unfocus, Button btn_focus){
        setFocus(btn_unfocus,btn_focus);
        sharedPreference_utils.setButtonFocusCustomMode(btn_focus.getId());
        this.btn_unfocus = btn_focus;
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
            btn_unfocus.setBackgroundResource(R.drawable.btn_unfocus);
            btn_unfocus.setTextColor(getResources().getColor(R.color.colorBlack));

            btn_focus.setBackgroundResource(R.drawable.btn_focus);
            btn_focus.setTextColor(getResources().getColor(R.color.colorWhite));


    }
    private void showDialogBrightnessCustomMode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.Brightness));
////      builder.setMessage(getString(R.string.ChangeBrightness));
//        builder.setIcon(R.drawable.ic_baseline_brightness_7_24_green);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewLayout = inflater.inflate(R.layout.dialog_brightness,null);
        SeekBar seekBar = viewLayout.findViewById(R.id.seekBarBrightness);
        setSeekBarForBrightness(seekBar);
        builder.setView(viewLayout);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void setSeekBarForBrightness(SeekBar seekBar) {
        seekBar.setMax(255);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(0);
        }
        seekBar.setProgress(sharedPreference_utils.getSeeSeekBarBrightness());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                setScreen_Brightness(seekBarProgress);
                int valuesSeekBarProgress = seekBarProgress * 100 /255;
                sharedPreference_utils.setNumberSeekBarBrightness(valuesSeekBarProgress);
                btnBrightnessCustomMode.setText(valuesSeekBarProgress+"%");
                sharedPreference_utils.setSeekBarBrightness(seekBarProgress);
            }
        });
    }
    private void setVolumeTurnOff() {
        AudioManager amanager = (AudioManager)getActivity().getSystemService(getActivity().AUDIO_SERVICE);
//        amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }
    private void setVolumeTurnOn() {
        AudioManager amanager = (AudioManager)getActivity().getSystemService(getActivity().AUDIO_SERVICE);
//        amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
        amanager.setStreamMute(AudioManager.STREAM_RING, false);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }
    private void eventBtnStopMode() {
        btnStopSleepMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(getActivity());
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
            }
        });
    }
    private void eventBtnStartSleepMode() {
        btnStartSleepMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(getActivity());
                timePicker.show(getActivity().getSupportFragmentManager(), "time picker");

            }
        });
    }
    private void setBluetoothTurnOff() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            Toast.makeText(getActivity(), "Your phone does not support bluetooth", Toast.LENGTH_SHORT).show();
        }else {
            adapter.disable();
        }

    }
    private void setScreen_Brightness(int screenBrightness) {
        android.provider.Settings.System.putInt(
                getActivity().getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, screenBrightness);
    }
    private void setVisibilityLinearLayout() {
        linearLayoutClassMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewClassicModel.setVisibility(View.GONE);
                linearLayoutClassMode.setVisibility(View.VISIBLE);
                linearLayoutClassMode_Line.setVisibility(View.VISIBLE);

                viewLongLifeMode.setVisibility(View.VISIBLE);
                linearLayoutLongLifeMode.setVisibility(View.GONE);
                linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);

                viewSleepModel.setVisibility(View.VISIBLE);
                linearLayoutSleepMode.setVisibility(View.GONE);
                linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);

                viewCustomMode.setVisibility(View.VISIBLE);
                linearLayoutCustomMode.setVisibility(View.GONE);
                linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutLongLifeMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewClassicModel.setVisibility(View.VISIBLE);
                linearLayoutClassMode.setVisibility(View.GONE);
                linearLayoutClassMode_Line.setVisibility(View.VISIBLE);


                viewLongLifeMode.setVisibility(View.GONE);
                linearLayoutLongLifeMode.setVisibility(View.VISIBLE);
                linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);

                viewSleepModel.setVisibility(View.VISIBLE);
                linearLayoutSleepMode.setVisibility(View.GONE);
                linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);

                viewCustomMode.setVisibility(View.VISIBLE);
                linearLayoutCustomMode.setVisibility(View.GONE);
                linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutSleepMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClassicModel.setVisibility(View.VISIBLE);
                linearLayoutClassMode.setVisibility(View.GONE);
                linearLayoutClassMode_Line.setVisibility(View.VISIBLE);


                viewLongLifeMode.setVisibility(View.VISIBLE);
                linearLayoutLongLifeMode.setVisibility(View.GONE);
                linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);

                viewSleepModel.setVisibility(View.GONE);
                linearLayoutSleepMode.setVisibility(View.VISIBLE);
                linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);

                viewCustomMode.setVisibility(View.VISIBLE);
                linearLayoutCustomMode.setVisibility(View.GONE);
                linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutCustomMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClassicModel.setVisibility(View.VISIBLE);
                linearLayoutClassMode.setVisibility(View.GONE);
                linearLayoutClassMode_Line.setVisibility(View.VISIBLE);

                viewLongLifeMode.setVisibility(View.VISIBLE);
                linearLayoutLongLifeMode.setVisibility(View.GONE);
                linearLayoutLongLifeMode_Line.setVisibility(View.VISIBLE);

                viewSleepModel.setVisibility(View.VISIBLE);
                linearLayoutSleepMode.setVisibility(View.GONE);
                linearLayoutSleepMode_Line.setVisibility(View.VISIBLE);


                viewCustomMode.setVisibility(View.GONE);
                linearLayoutCustomMode.setVisibility(View.VISIBLE);
                linearLayoutCustomMode_Line.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setScreenTimeout(int millisecounds) {
        android.provider.Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, millisecounds);
    }

    private  void alertDialogPermission_WriteSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getString(R.string.Permissionwritesettings));
        alertDialogBuilder
                .setMessage(getString(R.string.Allowpermissionwrite))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK),
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        int getNumberSeekBarBrightness =  sharedPreference_utils.getNumberSeekBarBrightness();
        if (btnBrightnessCustomMode !=null){
            btnBrightnessCustomMode.setText(getNumberSeekBarBrightness+"%");
        }
        getActivity().registerReceiver(batteryReceiverClass, intentFilter_WIFI_STATE_CHANGED_ACTION);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiverClass);
        super.onPause();
    }
}

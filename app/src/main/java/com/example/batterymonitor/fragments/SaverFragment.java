package com.example.batterymonitor.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batterymonitor.R;
import com.example.batterymonitor.Utils.SizeNumber;
import com.example.batterymonitor.activity.HomeActivity;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saver, container, false);

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


        radioButtonClassicMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdClassicMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (Settings.System.canWrite(getActivity())){
                        if (checkIdClassicMode) {
                            radioButtonLongLifeMode.setChecked(false);
                            radioButtonSleepMode.setChecked(false);
                            radioButtonCustomMode.setChecked(false);
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
                            ///setVisibility
                            linearLayoutClassMode_Line.setVisibility(View.GONE);
                            linearLayoutClassMode.setVisibility(View.VISIBLE);
                            ///setScreenTimeout
                            setScreenTimeout(SizeNumber.Thirty_seconds);
                            ////SCREEN_BRIGHTNESS
                            setScreen_Brightness(SizeNumber.namnoiphantram);

                            ////setAudioManager
                            setAudioManager();
                            ///setBluetoothDisable
                            setBluetoothTurnOff();

                        }
                    }
                    else {
                        radioButtonClassicMode.setChecked(false);
                        alertDialogPermission();
                    }

                }
            }
        });
        radioButtonLongLifeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdLongLifeMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (Settings.System.canWrite(getActivity())){
                        if (checkIdLongLifeMode) {
                            radioButtonClassicMode.setChecked(false);
                            radioButtonSleepMode.setChecked(false);
                            radioButtonCustomMode.setChecked(false);
                            ////setColorTextView
                            TypedValue typedValue = new TypedValue();
                            getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
                            int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
                            txtLongLifeMode.setTextColor(getResources().getColor(R.color.colorGreen));
                            txtClassicMode.setTextColor(color);
                            txtSleepMode.setTextColor(color);
                            txtCustomMode.setTextColor(color);
                            ////setColorImageView
                            imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24);
                            imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24_color_green);
                            imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
                            imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24);
                            ///setVisibility
                            linearLayoutLongLifeMode_Line.setVisibility(View.GONE);
                            linearLayoutLongLifeMode.setVisibility(View.VISIBLE);
                            ///setScreenTimeout
                            setScreenTimeout(SizeNumber.Fifteen_seconds);
                            ///setScreen_Brightness
                            setScreen_Brightness(SizeNumber.haimuoiphantram);

                            ////setAudioManager
                            setAudioManager();
                            ///setBluetoothDisable
                            setBluetoothTurnOff();

//                            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                            if(wifiManager.isWifiEnabled()){
//                                wifiManager.setWifiEnabled(false);
//                            }else{
//                                wifiManager.setWifiEnabled(true);
//                            }
                        }
                    }else {
                        radioButtonLongLifeMode.setChecked(false);
                        alertDialogPermission();
                    }
                }
            }
        });
        radioButtonSleepMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdSleepMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (Settings.System.canWrite(getActivity())){
                        if (checkIdSleepMode) {
                            radioButtonClassicMode.setChecked(false);
                            radioButtonLongLifeMode.setChecked(false);
                            radioButtonCustomMode.setChecked(false);
                            ////setColorTextView
                            TypedValue typedValue = new TypedValue();
                            getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
                            int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
                            txtSleepMode.setTextColor(getResources().getColor(R.color.colorGreen));
                            txtClassicMode.setTextColor(color);
                            txtLongLifeMode.setTextColor(color);
                            txtCustomMode.setTextColor(color);
                            ////setColorImageView
                            imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24);
                            imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                            imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24_color_green);
                            imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24);
                            ///setVisibility
                            linearLayoutSleepMode_Line.setVisibility(View.GONE);
                            linearLayoutSleepMode.setVisibility(View.VISIBLE);
                            setScreenTimeout(SizeNumber.Fifteen_seconds);
                            ///setScreen_Brightness
                            setScreen_Brightness(SizeNumber.haimuoiphantram);
                            ////setAudioManager
                            setAudioManager();
                            ///setBluetoothDisable
                            setBluetoothTurnOff();


                        }
                    }else {
                        radioButtonSleepMode.setChecked(false);
                        alertDialogPermission();
                    }

                }

            }
        });
        radioButtonCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkIdCustomMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (Settings.System.canWrite(getActivity())){
                        if (checkIdCustomMode) {
                            radioButtonClassicMode.setChecked(false);
                            radioButtonLongLifeMode.setChecked(false);
                            radioButtonSleepMode.setChecked(false);
                            ////setColorTextView
                            TypedValue typedValue = new TypedValue();
                            getActivity().getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
                            int color = ContextCompat.getColor(getActivity(), typedValue.resourceId);
                            txtCustomMode.setTextColor(getResources().getColor(R.color.colorGreen));
                            txtClassicMode.setTextColor(color);
                            txtLongLifeMode.setTextColor(color);
                            txtSleepMode.setTextColor(color);
                            ////setColorImageView
                            imgClassicMode.setImageResource(R.drawable.ic_baseline_offline_bolt_24);
                            imgLongLifeMode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                            imgSleepMode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
                            imgCusTomMode.setImageResource(R.drawable.ic_baseline_color_lens_24_color_green);
                            ///setVisibility
                            linearLayoutCustomMode_Line.setVisibility(View.GONE);
                            linearLayoutCustomMode.setVisibility(View.VISIBLE);
                        }
                    }else {
                        radioButtonCustomMode.setChecked(false);
                        alertDialogPermission();
                    }

                }

            }
        });

        setVisibilityLinearLayout();
        return view;
    }

    private void setBluetoothTurnOff() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.disable();
    }

    private void setAudioManager() {
        AudioManager amanager = (AudioManager)getActivity().getSystemService(getActivity().AUDIO_SERVICE);
//        amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);

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
                linearLayoutClassMode_Line.setVisibility(View.GONE);
                linearLayoutClassMode.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutLongLifeMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutLongLifeMode_Line.setVisibility(View.GONE);
                linearLayoutLongLifeMode.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutSleepMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutSleepMode_Line.setVisibility(View.GONE);
                linearLayoutSleepMode.setVisibility(View.VISIBLE);
            }
        });
        linearLayoutCustomMode_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutCustomMode_Line.setVisibility(View.GONE);
                linearLayoutCustomMode.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setScreenTimeout(int millisecounds) {
        android.provider.Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, millisecounds);
//        Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,millisecounds);
    }
    private  void alertDialogPermission() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Permission write settings !");
        alertDialogBuilder
                .setMessage("Allow permission write settings for your system ?")
                .setCancelable(false)
                .setPositiveButton("ALLOW",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

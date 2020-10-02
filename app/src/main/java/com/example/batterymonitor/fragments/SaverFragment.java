package com.example.batterymonitor.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batterymonitor.MainActivity;
import com.example.batterymonitor.R;
import com.example.batterymonitor.receiver.BatteryReceiver;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaverFragment.
     */
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


    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saver, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiver);
        super.onPause();
    }
}
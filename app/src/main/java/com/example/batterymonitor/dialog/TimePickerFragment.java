package com.example.batterymonitor.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.batterymonitor.R;
import com.example.batterymonitor.activity.HomeActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private Context context;

    public TimePickerFragment(Context context) {
        this.context = context;
    }

    private Button btnStartSleepMode,btnStopSleepMode;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        btnStartSleepMode = ((HomeActivity)context).findViewById(R.id.btnStartSleepMode);
        btnStopSleepMode = ((HomeActivity)context).findViewById(R.id.btnStopSleepMode);
        Calendar calendar = Calendar.getInstance();
        int hours  = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btnStartSleepMode.setText(hourOfDay+":"+ minute);
                btnStopSleepMode.setText(hourOfDay+":"+ minute);

                Log.d("sadas",""+hourOfDay+""+minute);
            }
        },hours, minute, DateFormat.is24HourFormat(getActivity()));
    }

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        Log.d("sadas",""+hourOfDay+""+minute);
//    }
}

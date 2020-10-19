package com.example.batterymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.batterymonitor.Utils.SizeNumber;
import com.example.batterymonitor.sharedPreference.SharedPreference_Utils;

public class MainActivity extends AppCompatActivity {
    private SharedPreference_Utils sharedPreference_utils;
    private Button btn15s,btn30s,btn1m,btn5m,btn10m,btn15m,btn20m,btn30m;
    private Button[] btn = new Button[8];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.btn15s, R.id.btn30s, R.id.btn1m, R.id.btn5m, R.id.btn10m, R.id.btn15m, R.id.btn20m, R.id.btn30m};
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreference_utils = new SharedPreference_Utils(this);
        btn15s = findViewById(R.id.btn15s);
        btn30s = findViewById(R.id.btn30s);
        btn1m = findViewById(R.id.btn1m);
        btn5m = findViewById(R.id.btn5m);
        btn10m =findViewById(R.id.btn10m);
        btn15m = findViewById(R.id.btn15m);
        btn20m =findViewById(R.id.btn20m);
        btn30m = findViewById(R.id.btn30m);

        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
        }
        btn_unfocus = btn[0];
        int idButton = sharedPreference_utils.getButtonFocusCustomMode();
        if (idButton  != 0){
            Button btnColorF = (Button) findViewById(idButton);
            setFocus(btn_unfocus,btnColorF);
        }
        btn15s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocus(btn_unfocus, btn[0]);
                Log.d("Buttons","btn15s");
            }
        });
        btn30s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[1]);
                Log.d("Buttons","btn30s");

            }
        });
        btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[2]);

                Log.d("Buttons","btn1m");
            }
        });
        btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[3]);
                Log.d("Buttons","btn5m");

            }
        });
        btn10m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[4]);
                Log.d("Buttons","btn10m");

            }
        });
        btn15m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[5]);
                Log.d("Buttons","btn15m");

            }
        });
        btn20m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[6]);
                Log.d("Buttons","btn20m");

            }
        });
        btn30m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFocus(btn_unfocus, btn[7]);

                Log.d("Buttons","btn30m");
            }
        });
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
        sharedPreference_utils.setButtonFocusCustomMode(btn_focus.getId());
    }

}
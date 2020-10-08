package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.batterymonitor.R;
import com.example.batterymonitor.service.ServiceNotifi;

public class TestActivity extends AppCompatActivity {
    private EditText editTextInput;
    private Button btnStart,btnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editTextInput = findViewById(R.id.edit_text_input);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);


    }

}
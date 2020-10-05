package com.example.batterymonitor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.batterymonitor.R;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout_Rate;
    private final String URL_Rate = "https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        relativeLayout_Rate = findViewById(R.id.relativeLayout_Rate);


        relativeLayout_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionIntent("https://play.google.com/store/apps/details?id=com.glgjing.hulk&hl=vi");
            }
        });
    }
    private void setActionIntent(String actionIntent) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(actionIntent));
        startActivity(i);
    }
}
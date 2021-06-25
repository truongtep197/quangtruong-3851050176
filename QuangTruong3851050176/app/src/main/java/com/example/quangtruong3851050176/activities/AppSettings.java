package com.example.quangtruong3851050176.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quangtruong3851050176.R;


public class AppSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_app_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.settings_title));
    }

}

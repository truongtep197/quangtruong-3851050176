package com.example.quangtruong3851050176.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quangtruong3851050176.R;
import com.example.quangtruong3851050176.helpers.SettingsHelper;


public class AppSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsHelper.applyTheme(this);
        setContentView(R.layout.activity_app_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        SettingsHelper.applyThemeToolbar(findViewById(R.id.toolbar), this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.settings_title));
    }

}

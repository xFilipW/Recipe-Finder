package com.example.recipefinder.introductory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.recipefinder.R;
import com.example.recipefinder.main.MainActivity;

public class IntroductoryActivity extends AppCompatActivity {

    private static final int timeout = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(IntroductoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, timeout);
    }
}
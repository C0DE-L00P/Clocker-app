package com.develoop.projectimer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class Alarm extends Activity {
    private static final String TAG = "Alarm";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        
    }
}

package com.develoop.projectimer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class TImerActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);

        findViewById(R.id.timerTimelapseBtn).setOnClickListener(this);
        findViewById(R.id.timerPauseBtn).setOnClickListener(this);
        findViewById(R.id.timerPlayBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timerPlayBtn:
                slide(findViewById(R.id.timerTopBelow)
                ,findViewById(R.id.timerTop)
                ,findViewById(R.id.timerMid)
                ,findViewById(R.id.timerBottom)
                ,findViewById(R.id.timerBottomBelow));
                break;
            case R.id.timerPauseBtn:
                break;
            case R.id.timerTimelapseBtn:
                break;
            default:
        }
    }

    void slide(View v1,View v2,View v3,View v4,View v5){
        v2.animate().scaleX(1.5f).scaleY(1.5f).translationYBy(v3.getMeasuredHeight());
        v3.animate().translationYBy(v3.getMeasuredHeight()).scaleX(.8f).scaleY(.8f);
        v4.animate().alpha(0);
        v1.animate().alpha(1);
    }
}

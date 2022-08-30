package com.develoop.projectimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Thread thread;
    int g, h, j, k, l, m;
    int[] tops = {R.mipmap.zero, R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four, R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight, R.mipmap.nine};
    int[] bottoms = {R.mipmap.zero1, R.mipmap.one1, R.mipmap.two1, R.mipmap.three1, R.mipmap.four1, R.mipmap.five1, R.mipmap.six1, R.mipmap.seven1, R.mipmap.eight1, R.mipmap.nine1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//
////        LocationListener locationListener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
////        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        //TODO: just an experiment
        Date _now = Calendar.getInstance().getTime();
        Time now = new Time();
        now.setToNow();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String currentSec = new SimpleDateFormat("ss", Locale.US).format(new Date());
                    final int currentTimeSecF = Integer.parseInt(currentSec)%10;
                    final int currentTimeSecS = Integer.parseInt(currentSec)/10;

                    String currentMin = new SimpleDateFormat("mm", Locale.US).format(new Date());
                    final int currentTimeMinF = Integer.parseInt(currentMin)%10;
                    final int currentTimeMinS = Integer.parseInt(currentMin)/10;

                    String currentHour = new SimpleDateFormat("hh", Locale.US).format(new Date());
                    final int currentTimeHourF = Integer.parseInt(currentHour)%10;
                    final int currentTimeHourS = Integer.parseInt(currentHour)/10;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowClock(String.valueOf(currentTimeSecF)
                                    ,String.valueOf(currentTimeSecS)
                                    ,String.valueOf(currentTimeMinF)
                                    ,String.valueOf(currentTimeMinS)
                                    ,String.valueOf(currentTimeHourF)
                                    ,String.valueOf(currentTimeHourS)
                            );
                        }
                    });
                    try {
                        thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        findViewById(R.id.stopwatchBtn).setOnClickListener(this);
        findViewById(R.id.climateBtn).setOnClickListener(this);
        findViewById(R.id.timerBtn).setOnClickListener(this);
        findViewById(R.id.alarmBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stopwatchBtn:
                startActivity(new Intent(this,StopWatch.class));
                break;
            case R.id.timerBtn:
                startActivity(new Intent(this,timerMessAround.class));
                break;
            default:
                Toast.makeText(this, "Still in progress", Toast.LENGTH_SHORT).show();
        }
    }

    void animateDown(final ImageView img1,final ImageView img2,final ImageView img3,final ImageView img4,final int res1,final int res2){

        img3.setImageResource(res1);
        img2.setImageResource(res2);
        img2.animate().alpha(1f);

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(img1, "scaleY", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(img2, "scaleY", 0f, 1f);
        img1.setPivotY(img1.getMeasuredHeight());
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                img1.setImageResource(res1);
                img1.animate().alpha(0f);
                img2.setPivotY(0);
                oa2.start();
                oa2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        img1.setScaleY(1f);
                        img1.animate().alpha(1f);
                        img4.setImageResource(res2);
                        img2.animate().alpha(0f);
                        img2.setScaleY(0f);
                    }
                });
            }
        });
        oa1.start();
    }

    void ShowClock(String sf,String ss,String mf,String ms,String hf,String hs){


        animateDown((ImageView) findViewById(R.id.top),(ImageView) findViewById(R.id.bottom),(ImageView) findViewById(R.id.holderTop),(ImageView) findViewById(R.id.holderBottom)
                ,tops[Integer.parseInt(sf)],bottoms[Integer.parseInt(sf)]);

        //Second ss
        if (Integer.parseInt(ss) != g){
            animateDown((ImageView) findViewById(R.id.top1),(ImageView) findViewById(R.id.bottom1),(ImageView) findViewById(R.id.holderTop1),(ImageView) findViewById(R.id.holderBottom1)
                    ,tops[Integer.parseInt(ss)],bottoms[Integer.parseInt(ss)]);
}
        g = Integer.parseInt(ss);

        //Min f
        if (Integer.parseInt(mf) != h){
            animateDown((ImageView) findViewById(R.id.top5),(ImageView) findViewById(R.id.bottom5),(ImageView) findViewById(R.id.holderTop5),(ImageView) findViewById(R.id.holderBottom5)
                    ,tops[Integer.parseInt(mf)],bottoms[Integer.parseInt(mf)]);}
        h = Integer.parseInt(mf);


        //Min s
        if (Integer.parseInt(ms) != j){
            animateDown((ImageView) findViewById(R.id.top4),(ImageView) findViewById(R.id.bottom4),(ImageView) findViewById(R.id.holderTop4),(ImageView) findViewById(R.id.holderBottom4)
                    ,tops[Integer.parseInt(ms)],bottoms[Integer.parseInt(ms)]);}
        j = Integer.parseInt(ms);


        //Hour f
        if (Integer.parseInt(hf) != k){
            animateDown((ImageView) findViewById(R.id.top3),(ImageView) findViewById(R.id.bottom3),(ImageView) findViewById(R.id.holderTop3),(ImageView) findViewById(R.id.holderBottom3)
                    ,tops[Integer.parseInt(hf)],bottoms[Integer.parseInt(hf)]);}
        k = Integer.parseInt(hf);


        //Hour s
        if (Integer.parseInt(hs) != l){
            animateDown((ImageView) findViewById(R.id.top2),(ImageView) findViewById(R.id.bottom2),(ImageView) findViewById(R.id.holderTop2),(ImageView) findViewById(R.id.holderBottom2)
                    ,tops[Integer.parseInt(hs)],bottoms[Integer.parseInt(hs)]);}
        l = Integer.parseInt(hs);


        }
}
/*

*/
/*---------- Listener class to get coordinates ------------- *//*

class MyLocationListener implements LocationListener {

    TextView editLocation;
    @Override
    public void onLocationChanged(Location loc) {
        editLocation.setText("");
        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(
                ,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("GPS", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("GPS", latitude);

        */
/*------- To get city name from coordinates -------- *//*

        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
        editLocation.setText(s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}*/

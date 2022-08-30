package com.develoop.projectimer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import static android.content.ContentValues.TAG;

public class timerMessAround extends Activity {
    private ViewPager2 parentViewpager, viewPager2;
    private Button startTimerBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_mess_around);
        startTimerBtn = findViewById(R.id.startTimerBtn);
        parentViewpager = findViewById(R.id.parentTimerViewpager);
        final ParentAdapter pa = new ParentAdapter();
        parentViewpager.setAdapter(pa);

        parentViewpager.setClipToPadding(false);
        parentViewpager.setClipChildren(false);
        parentViewpager.setOffscreenPageLimit(3);
        parentViewpager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - position;
                float m = 1 - Math.abs(position);
                page.animate().scaleX(.65f + .35f * r).scaleY(.65f + .35f * r).alpha(.30f + .70f * m).setDuration(0);
            }
        });
        parentViewpager.setPageTransformer(compositePageTransformer);

        startTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pa.runnable.run();
                if (!pa.isTimerOn){
                    pa.isTimerOn = true;
                    pa.handlerSec.postDelayed(pa.runnableSec, 1000);
                }else{
                    pa.isTimerOn = false;
                }
            }
        });

    }
}

class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    int[] hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    int[] non_hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30
            , 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};


    boolean isTimerOn;
    Handler handlerSec;
    Runnable runnableSec, runnableMin, runnableHour;

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timers_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ParentViewHolder holder, int position) {

        if (position == 0) {
            TimerSliderAdapter tsa = new TimerSliderAdapter(hours, 80);
            holder.vp.setAdapter(tsa); //TODO here we are
            runnableHour = new Runnable() {
                @Override
                public void run() {
                    if (holder.vp.getCurrentItem() != 0) {
                        holder.vp.setCurrentItem(holder.vp.getCurrentItem() - 1);
                    } else {
                        try {
                            Toast.makeText(holder.itemView.getContext(), "Achievement I think :)", Toast.LENGTH_SHORT).show();
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            Ringtone r = RingtoneManager.getRingtone(holder.itemView.getContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isTimerOn = false;
                    }
                }
            };


            holder.vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (isTimerOn) holder.vp.getCurrentItem();
                }
            });

        } else if (position == 1) {
            holder.vp.setAdapter(new TimerSliderAdapter(non_hours, 80));
            runnableMin = new Runnable() {
                @Override
                public void run() {
                    if (isTimerOn) {
                        if (holder.vp.getCurrentItem() != 0) {
                            holder.vp.setCurrentItem(holder.vp.getCurrentItem() - 1);
                        } else {
                            runnableHour.run();
                            if (isTimerOn)holder.vp.setCurrentItem(non_hours.length);
                        }
                    }
                }
            };

        } else {
            holder.vp.setAdapter(new TimerSliderAdapter(non_hours, 80));
            handlerSec = new Handler();
            runnableSec = new Runnable() {
                @Override
                public void run() {
                    if (isTimerOn) {
                        if (holder.vp.getCurrentItem() != 0) {
                            holder.vp.setCurrentItem(holder.vp.getCurrentItem() - 1);
                        } else {
                            runnableMin.run();
                            if (isTimerOn) holder.vp.setCurrentItem(non_hours.length);
                        }
                        handlerSec.postDelayed(runnableSec, 1000);
                    }
                }
            };
        }

        holder.vp.setClipToPadding(false);
        holder.vp.setClipChildren(false);
        holder.vp.setOffscreenPageLimit(3);
        holder.vp.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                ((TextView) page.findViewById(1)).animate().alpha(.65f + .35f * r).setDuration(0);
                ((TextView) page.findViewById(1)).setTextSize(54 + 26 * r);
            }
        });
        holder.vp.setPageTransformer(compositePageTransformer);
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class ParentViewHolder extends RecyclerView.ViewHolder {
        ViewPager2 vp;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            vp = itemView.findViewById(R.id.timerViewpager);
        }
    }
}


class TimerSliderAdapter extends RecyclerView.Adapter<TimerSliderAdapter.TimerViewHolder> {

    private int[] times;
    private int textSize;

    public TimerSliderAdapter(int[] times, int textSize) {
        this.times = times;
        this.textSize = textSize;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout ll = new LinearLayout(parent.getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView tvo = new TextView(parent.getContext());
        tvo.setGravity(Gravity.CENTER);
        tvo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvo.setTextSize(textSize);
        tvo.setTypeface(Typeface.DEFAULT_BOLD);
        tvo.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
        tvo.setId(1);
        ll.addView(tvo);
        return new TimerViewHolder(ll);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("00");
        holder.tv.setText(df.format(times[position]));
    }

    @Override
    public int getItemCount() {
        return times.length;
    }

    class TimerViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(1);
        }
    }
}

package com.develoop.projectimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StopWatch extends Activity implements View.OnClickListener, RecordsRecyclerAdapter.RecordsListener {
    String TAG = "DaTuM";
    private Thread stopwatch;
    private int mil,sec,min,q,w;
    private boolean timerBool = true;
    private TextView stopwatchMilTxt,stopwatchSecTxt,stopwatchMinTxt;
    private RecyclerView rvRecords;
    private ArrayList<String> recordsList = new ArrayList<>();
    private RecordsRecyclerAdapter rra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_bottomsheet);

        rvRecords = findViewById(R.id.records_recycler);
        rra = new RecordsRecyclerAdapter(recordsList,this);
        rvRecords.setAdapter(rra);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        rvRecords.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.d(TAG, "onMove: it just moved");
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onMove: it just swiped " + direction);
                recordsList.remove(viewHolder.getAdapterPosition());
                rra.notifyDataSetChanged();
                if (recordsList.size() > 0){
                    findViewById(R.id.records_counter).animate().alpha(1);
                    ((TextView)findViewById(R.id.records_counter)).setText(String.valueOf(recordsList.size()));
                }else{
                    findViewById(R.id.records_counter).animate().alpha(0);
                }
            }
        });

        ith.attachToRecyclerView(rvRecords);


//
//        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP,ItemTouchHelper.DOWN) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
//                int postition_dragged = dragged.getAdapterPosition();
//                int postition_target = target.getAdapterPosition();
//                Collections.swap(recordsList,postition_dragged,postition_target);
//                rra.notifyItemMoved(postition_dragged,postition_target);
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            }
//        });
//
//        ith.attachToRecyclerView(rvRecords);

        min = mil = sec =q=w=0;
        findViewById(R.id.timerPlayBtn).setOnClickListener(this);
        findViewById(R.id.timerPauseBtn).setOnClickListener(this);
        findViewById(R.id.timerTimelapseBtn).setOnClickListener(this);
         stopwatchMilTxt = findViewById(R.id.stopwatchMilTxt);
         stopwatchSecTxt = findViewById(R.id.stopwatchSecTxt);
         stopwatchMinTxt = findViewById(R.id.timerMid);

        stopwatch = new Thread(new Runnable() {
            @Override
            public void run() {

                while(timerBool){

                    mil = mil+10;
                    if (mil==100){
                        mil = 0;
                        sec = sec+1;
                    }

                    if (sec==60){
                        sec=0;
                        min=min+1;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                            DecimalFormat form = (DecimalFormat) nf;
                            form.applyPattern("00");
                            stopwatchMilTxt.setText(form.format(mil));
                            stopwatchSecTxt.setText(form.format(sec));
                            stopwatchMinTxt.setText(form.format(min));
                        }
                    });

                    q=sec;
                    w=min;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timerPlayBtn:
                if(stopwatch.isAlive()){
                    resetTimer();
                    ((ImageButton)v).setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    v.setBackgroundResource(R.drawable.circle_bg2);
                }else{
                    stopwatch.start();
                    timerBool = true;
                    ((ImageButton)v).setImageResource(R.drawable.ic_baseline_stop_24);
                    v.setBackgroundResource(R.drawable.circle_bg);
                }
                break;
            case R.id.timerPauseBtn:
                timerBool = false;
                ((ImageButton) findViewById(R.id.timerPlayBtn)).setImageResource(R.drawable.ic_baseline_play_arrow_24);
                (findViewById(R.id.timerPlayBtn)).setBackgroundResource(R.drawable.circle_bg2);
                break;
            case R.id.timerTimelapseBtn:
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;
                df.applyPattern("00");
                recordsList.add(df.format(min)+":"+df.format(sec)+":"+df.format(mil));
                rra.notifyDataSetChanged();
                if (recordsList.size() > 0){
                    findViewById(R.id.records_counter).animate().alpha(1);
                    ((TextView)findViewById(R.id.records_counter)).setText(String.valueOf(recordsList.size()));
                }else{
                    findViewById(R.id.records_counter).animate().alpha(0);
                }
                break;
            default:
                break;
        }
    }

    void resetTimer(){
        timerBool =false;
        min = mil = sec = q = w =0;

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat form = (DecimalFormat) nf;
        form.applyPattern("00");
        stopwatchMilTxt.setText(form.format(mil));
        stopwatchSecTxt.setText(form.format(sec));
        stopwatchMinTxt.setText(form.format(min));
    }

    @Override
    public void recordClick(int position) {
        recordsList.remove(position);
        rra.notifyDataSetChanged();
        if (recordsList.size() > 0){
            findViewById(R.id.records_counter).animate().alpha(1);
            ((TextView)findViewById(R.id.records_counter)).setText(String.valueOf(recordsList.size()));
        }else{
            findViewById(R.id.records_counter).animate().alpha(0);
        }
    }
}


class RecordsRecyclerAdapter extends RecyclerView.Adapter<RecordsRecyclerAdapter.RecordsViewHolder>{

    private ArrayList<String> recordsList;
    private RecordsListener mRecordsListener;

    public RecordsRecyclerAdapter(ArrayList<String> recordsList,RecordsListener recordsListener) {
        this.recordsList = recordsList;
        this.mRecordsListener = recordsListener;
    }

    @NonNull
    @Override
    public RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_item,null);
        return new RecordsViewHolder(new TextView(parent.getContext()),mRecordsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        holder.tv.setText(recordsList.get(position));
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView tv;
        RecordsListener mRecordsListener;
        public RecordsViewHolder(@NonNull View itemView,RecordsListener recordsListener) {
            super(itemView);
            tv = (TextView) itemView;
            tv.setTextSize(30);
            tv.setPadding(30,0,30,0);
            tv.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
            tv.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);

            itemView.setOnLongClickListener(this);
            this.mRecordsListener = recordsListener;
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    //TODO: zyada eb2a shelha
    interface RecordsListener{
        void recordClick(int position);
    }
}

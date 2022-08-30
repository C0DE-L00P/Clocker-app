package com.develoop.projectimer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimerActivity2 extends Activity {
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity2);

        rv = findViewById(R.id.rv);
        rv.setAdapter(new CustomRecyclerAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}

class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder>{

    String[] timerList = {"00","01","02","03","04","05","06","07","08","09","10","11","12"};

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_item,null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv.setText(timerList[position]);
    }

    @Override
    public int getItemCount() {
        return timerList.length;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.timerTime);
        }
    }
}

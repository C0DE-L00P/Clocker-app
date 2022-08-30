package com.develoop.projectimer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

public class viewPager2Test extends Activity {

    private ArrayList<String> imgList = new ArrayList<>();
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        imgList.add("https://19d3782b340d3615223a-8b8ea69741e8d4c89e10e075b4ccc0a2.ssl.cf3.rackcdn.com/6158_t_f907d6c2-da03-4834-aff9-dd23f9660d85.jpg");
        imgList.add("https://api.newyorker.de/blob/cms-lifestyle/2f24b73a635b47082305e0eb157da859.jpg");
        imgList.add("https://dvyvvujm9h0uq.cloudfront.net/com/articles/1559139466-modelposes.jpg");
        imgList.add("https://lh3.googleusercontent.com/proxy/_FRLCfhz4VWqzOiIr62ow4aYpp9L1DPHU22kH1jUOsWIZxe8F5ALdYkgEMhATUB8RrHcylUIta62wVGYMEUzUVYs8OVh1Qak-cObLtIQK-iutur7YFPUDsdX4hR42ignVTKRX2ELYN5MJZU");
        imgList.add("https://i.pinimg.com/originals/e8/1b/71/e81b715bae343143bba881a3443ff940.jpg");

        viewPager2 = findViewById(R.id.viewpagerImgSlider);
        viewPager2.setAdapter(new SliderAdapter(imgList, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(.85f + r*.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPager2.removeCallbacks(runnable);
                viewPager2.postDelayed(runnable,3000);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        viewPager2.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager2.postDelayed(runnable,3000);
    }
}

class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private ArrayList<String> imgList;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(ArrayList<String> imgList, ViewPager2 viewPager2) {
        this.imgList = imgList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ImageView o = new ImageView(context);
        o.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return new SliderViewHolder(o);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        if (position == (imgList.size() - 2))viewPager2.post(runnable);
        Glide.with(context).load(imgList.get(position)).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgList.addAll(imgList);
            notifyDataSetChanged();
        }
    };
}

class SliderItem {
    String imgUrl;

    public SliderItem(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
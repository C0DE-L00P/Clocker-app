package com.develoop.projectimer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

public class repeatViewPager2 extends Activity {

    ViewPager2 viewPager2;
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        viewPager2 =findViewById(R.id.viewpagerImgSlider);
        list.add("https://19d3782b340d3615223a-8b8ea69741e8d4c89e10e075b4ccc0a2.ssl.cf3.rackcdn.com/6158_t_f907d6c2-da03-4834-aff9-dd23f9660d85.jpg");
        list.add("https://api.newyorker.de/blob/cms-lifestyle/2f24b73a635b47082305e0eb157da859.jpg");
        list.add("https://dvyvvujm9h0uq.cloudfront.net/com/articles/1559139466-modelposes.jpg");
        list.add("https://lh3.googleusercontent.com/proxy/_FRLCfhz4VWqzOiIr62ow4aYpp9L1DPHU22kH1jUOsWIZxe8F5ALdYkgEMhATUB8RrHcylUIta62wVGYMEUzUVYs8OVh1Qak-cObLtIQK-iutur7YFPUDsdX4hR42ignVTKRX2ELYN5MJZU");
        list.add("https://i.pinimg.com/originals/e8/1b/71/e81b715bae343143bba881a3443ff940.jpg");

        viewPager2.setAdapter(new SliderAdapter2(list,viewPager2));

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
                page.setScaleX(.85f + r*.15f);
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

class SliderAdapter2 extends RecyclerView.Adapter<SliderAdapter2.SliderViewHolder2>{

    private ArrayList<String> list;
    private Context ctx;
    private ViewPager2 viewPager2;

    public SliderAdapter2(ArrayList<String> list,ViewPager2 viewPager2) {
        this.list = list;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        ImageView ivo = new ImageView(ctx);
        ivo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new SliderViewHolder2(ivo);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder2 holder, int position) {
        if (position == list.size()-2) viewPager2.post(new Runnable() {
            @Override
            public void run() {
                list.addAll(list);
                notifyDataSetChanged();
            }
        });
        Glide.with(ctx).load(list.get(position)).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SliderViewHolder2 extends RecyclerView.ViewHolder{
        private ImageView iv;
        public SliderViewHolder2(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView;
        }
    }
}
package com.projectbelajar.yuukbelajar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.projectbelajar.yuukbelajar.adapter.ViewPagerAdapter;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class NewLayoutActivity extends AppCompatActivity {

    private static ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<String> carouselBanners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new);

        //        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
//        adapter = new ViewPagerAdapter(carouselBanners, this);
//        adapter.notifyDataSetChanged();
//        viewPager.setAdapter(adapter);
//        dotsIndicator.setViewPager(viewPager);
    }
}
package com.projectbelajar.yuukbelajar;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.kelin.translucentbar.library.TranslucentBarManager;
import com.projectbelajar.yuukbelajar.adapter.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;


public class NewActivity extends AppCompatActivity {

    private static ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<String> carouselBanners = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_course);

        TranslucentBarManager translucentBarManager = new TranslucentBarManager(this);
        translucentBarManager.transparent(this, R.color.colorPrimaryDark);

//        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
//        adapter = new ViewPagerAdapter(carouselBanners, this);
//        adapter.notifyDataSetChanged();
//        viewPager.setAdapter(adapter);
//        dotsIndicator.setViewPager(viewPager);
    }
}

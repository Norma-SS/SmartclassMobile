package com.projectbelajar.yuukbelajar;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.projectbelajar.yuukbelajar.fragment.CourseFragment;
import com.projectbelajar.yuukbelajar.fragment.ProfileFragment;
import com.projectbelajar.yuukbelajar.fragment.UksFragment;

@SuppressWarnings("deprecation")
public class Tabbed extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

//                    if (getIntent().getExtras().getString("level").equals("DOKTER")) {
//                        switch (item.getItemId()) {
//                            case R.id.nav_profile:
//                                selectedFragment = new ProfileFragment();
//                                break;
//                            case R.id.nav_more:
//                                selectedFragment = new UksFragment();
//                                break;
//                        }
//
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_dokter,
//                                selectedFragment).commit();
//
//                    } else {
                        switch (item.getItemId()) {
                            case R.id.nav_profile:
                                selectedFragment = new ProfileFragment();
                                break;
                            case R.id.nav_home:
                                selectedFragment = new CourseFragment();
                                break;
                            case R.id.nav_more:
                                selectedFragment = new UksFragment();
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
//                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getIntent().getExtras().getString("level").equals("DOKTER")){
//            setContentView(R.layout.activity_tabbed_dokter);
//            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_dokter);
//            bottomNav.setOnNavigationItemSelectedListener(navListener);
//            bottomNav.setSelectedItemId(R.id.nav_more);
//        } else {
            setContentView(R.layout.activity_tabbed2);
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
            bottomNav.setSelectedItemId(R.id.nav_home);
//        }
//        spaceNavigationView = findViewById(R.id.space);
//        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
//        spaceNavigationView.setCentreButtonSelectable(true);
//        spaceNavigationView.setCentreButtonSelected();
//        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_home_24);
//        spaceNavigationView.addSpaceItem(new SpaceItem("UKS", R.drawable.icon_uks));
//        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.ic_person_24));
//        spaceNavigationView.showTextOnly();
//        if (savedInstanceState == null && getIntent().getExtras().getString("level").equals("DOKTER")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_dokter,
//                    new UksFragment()).commit();
//        }else
            if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CourseFragment()).commit();
        }
//        Intent intent;

//        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//                setFragment(new CourseFragment());
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//                switch (itemIndex) {
//                    case 0:
//                        setFragment(new UksFragment());
//                        break;
//                    case 1:
//                        setFragment(new ProfileFragment());
//                        break;
//                }
//                Toast.makeText(Tabbed.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//                Toast.makeText(Tabbed.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//        });
//        intent = new Intent().setClass(this, CourseActivity.class);//content pada tab yang akan kita buat
//        spec = tabhost.newTabSpec("Beranda").setIndicator("Beranda",null).setContent(intent);//mengeset nama tab dan mengisi content pada menu tab anda.
//        tabhost.addTab(spec);//untuk membuat tabbaru disini bisa diatur sesuai keinginan anda
//
//        intent = new Intent().setClass(this, tampilan.class);
//        spec = tabhost.newTabSpec("Profil").setIndicator("Profil",null).setContent(intent);
//        tabhost.addTab(spec);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);    }
}
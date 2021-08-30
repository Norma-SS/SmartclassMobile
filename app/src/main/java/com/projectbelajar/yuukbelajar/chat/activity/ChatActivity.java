package com.projectbelajar.yuukbelajar.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectbelajar.yuukbelajar.AboutActivity;
import com.projectbelajar.yuukbelajar.Login;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.Tabbed;
import com.projectbelajar.yuukbelajar.TabbedDoktor;
import com.projectbelajar.yuukbelajar.TabbedGuru;
import com.projectbelajar.yuukbelajar.TabbedKepsek;
import com.projectbelajar.yuukbelajar.chat.adapter.ViewPagerChatAdapter;
import com.projectbelajar.yuukbelajar.chat.fragment.ChatsFragment;
import com.projectbelajar.yuukbelajar.chat.fragment.UserChatsFragment;
import com.projectbelajar.yuukbelajar.chat.model.Chater;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatActivity extends AppCompatActivity {
    private RecyclerView rvChater;
    private ArrayList<Chater> chaters = new ArrayList<>();
//    private Product product;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private TextView toolbarTitle;
    private Preferences preferences;

    private String chatType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        preferences = new Preferences(this);

        chatType = preferences.getValues("chatType");


        Toolbar tb = findViewById(R.id.main_tb);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chatting");
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (preferences.getValues("level")) {
                    case "DOKTER": {
                        Intent j = new Intent(ChatActivity.this, TabbedDoktor.class);
                        startActivity(j);
                        break;
                    }
                    case "GURU":
                    case "WALIKELAS": {
                        Intent j = new Intent(ChatActivity.this, TabbedGuru.class);
                        startActivity(j);
                        break;
                    }
                    case "KS": {
                        Intent j = new Intent(ChatActivity.this, TabbedKepsek.class);
                        startActivity(j);
                        break;
                    }
                    default: {
                        Intent j = new Intent(ChatActivity.this, Tabbed.class);
                        startActivity(j);
                        break;
                    }
                }
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String level = preferences.getValues("level");

        TabLayout tabLayout = findViewById(R.id.tablayout_chat);
        ViewPager viewPager = findViewById(R.id.viewpager_chat);
        ViewPagerChatAdapter viewPagerChatAdapter = new ViewPagerChatAdapter(getSupportFragmentManager());
//        viewPagerChatAdapter.addFragment(new NoDataFragment(), "");

//        viewPagerChatAdapter.addFragment(new NoDataFragment(), "STATUS");
        String tabTitle = "";



        if (chatType.equals("walikelas")){
            if (level.equals("WALIKELAS") || level.equals("GURU")){
                tabTitle = "Murid";
            }else if (level.equals("SISWA") || level.equals("WALI MURID") || level.equals("ORANG TUA")){
                tabTitle = "Walikelas";
            }
        }else{
            if (level.equals("DOKTER")){
                tabTitle = "Pasien";
            }else if (level.equals("SISWA") || level.equals("WALI MURID") || level.equals("ORANG TUA") || level.equals("KS")){
                tabTitle = "Dokter";
            }
        }


        toolbarTitle.setText("Chat "+tabTitle);
        viewPagerChatAdapter.addFragment(new ChatsFragment(), "Obrolan");
        viewPagerChatAdapter.addFragment(new UserChatsFragment(), tabTitle);
        viewPager.setAdapter(viewPagerChatAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == android.R.id.home) {// todo: goto back activity from here
//
////            FirebaseAuth.getInstance().signOut();
//
//
//
////            Intent intent = new Intent(ChatActivity.this, Tabbed.class);
////            startActivity(intent);
////            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("dtUsers").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        status("offline");
    }
}
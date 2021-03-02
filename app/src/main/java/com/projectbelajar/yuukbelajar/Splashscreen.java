package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import static com.projectbelajar.yuukbelajar.SessionManager.is_login;

public class Splashscreen extends AppCompatActivity {
    SessionManager sessionManager;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

        sessionManager = new SessionManager(getApplicationContext());
        preferences = new Preferences(getApplicationContext());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("isLogin ", " "+preferences.getBoolValue("isLogin"));
                if (!preferences.getBoolValue("isLogin")) {
                    Intent i = new Intent(Splashscreen.this, Login.class);
                    startActivity(i);
                    finish();
                } else {
                    switch (preferences.getValues("level")) {
                        case "DOKTER": {
                            Intent i = new Intent(Splashscreen.this, TabbedDoktor.class);
                            startActivity(i);
                            finish();
                            break;
                        }
                        case "KS": {
                            Intent i = new Intent(Splashscreen.this, TabbedKepsek.class);
                            startActivity(i);
                            finish();
                            break;
                        }
                        case "GURU":
                        case "WALIKELAS": {
                            Intent i = new Intent(Splashscreen.this, TabbedGuru.class);
                            startActivity(i);
                            finish();
                            break;
                        }
                        default: {
                            Intent i = new Intent(Splashscreen.this, Tabbed.class);
                            startActivity(i);
                            finish();
                            break;
                        }
                    }
                }

            }
        }, 3000L);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed(); // go previous activity
//        return super.onSupportNavigateUp();
//    }
}

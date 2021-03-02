package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Profil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "tombol back ditekan", Toast.LENGTH_SHORT).show();
        //finish();
        Intent i = new Intent(Profil.this, Tabbed.class);
        startActivity(i);
    }
}

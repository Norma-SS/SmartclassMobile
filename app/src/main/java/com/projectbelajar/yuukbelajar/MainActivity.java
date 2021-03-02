package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener {
    //private Button pesanReg;
    private TextView pesanReg;
    private Button pesanLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pesanReg = (TextView) findViewById(R.id.btn_reg);
        pesanReg.setOnClickListener(this);
        pesanLogin = (Button) findViewById(R.id.btn_login);
        pesanLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_reg:
                pesanReg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, Registrasi.class);
                        startActivity(i);
                    }
                });
                break;
            case R.id.btn_login:
                pesanLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                    }
                });
                break;
        }
    }

}

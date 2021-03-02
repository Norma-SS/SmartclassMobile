package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.projectbelajar.yuukbelajar.SessionManager.password;
import static java.nio.file.Paths.get;

public class MenuElearning extends AppCompatActivity  implements View.OnClickListener{
    private TextView editTextKls;
    private TextView editTextSkl;
    private String eml, materi;
    private TextView Txt1, Txt2, Txt3, Txt4, Txt5, Txt6, Txt7, Txt8, Txt9, Txt10, Txt11, Txt12;

    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private ImageView Image4;
    private ImageView Image5;
    private ImageView Image6;
    private ImageView Image7;
    private ImageView Image8;
    private ImageView Image9;
    private ImageView Image10;
    private ImageView Image11;
    private ImageView Image12;

    SessionManager sessionManager;

    private static final String pref_name = "crudpref";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_elearning);

        editTextSkl = (TextView) findViewById(R.id.NmSkl);
        editTextKls = (TextView) findViewById(R.id.Kls);

        Txt1 = (TextView) findViewById(R.id.txt1);
        Txt1.setOnClickListener(this);
        Txt2 = (TextView) findViewById(R.id.txt2);
        Txt2.setOnClickListener(this);
        Txt3 = (TextView) findViewById(R.id.txt3);
        Txt3.setOnClickListener(this);
        Txt4 = (TextView) findViewById(R.id.txt4);
        Txt4.setOnClickListener(this);
        Txt5 = (TextView) findViewById(R.id.txt5);
        Txt5.setOnClickListener(this);
        Txt6 = (TextView) findViewById(R.id.txt6);
        Txt6.setOnClickListener(this);
        Txt7 = (TextView) findViewById(R.id.txt7);
        Txt7.setOnClickListener(this);
        Txt8 = (TextView) findViewById(R.id.txt8);
        Txt8.setOnClickListener(this);
        Txt9 = (TextView) findViewById(R.id.txt9);
        Txt9.setOnClickListener(this);
        Txt10 = (TextView) findViewById(R.id.txt10);
        Txt10.setOnClickListener(this);
        Txt11 = (TextView) findViewById(R.id.txt11);
        Txt11.setOnClickListener(this);
        Txt12 = (TextView) findViewById(R.id.txt12);
        Txt12.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);
        //Toast.makeText(getApplicationContext(), "Emailku.."+eml, Toast.LENGTH_LONG).show();

        getEmployee();
    }

    private void getEmployee(){
        //Toast.makeText(getApplicationContext(), eml, Toast.LENGTH_LONG).show();
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MenuElearning.this,"","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();

    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            //String id = c.getString(konfigurasi.TAG_ID);
            String name = c.getString(konfigurasi.TAG_NAMA);
            String desg = c.getString(konfigurasi.TAG_POSISI);
            String kls = c.getString(konfigurasi.TAG_KLS);
            String uts = c.getString(konfigurasi.TAG_UTS);
            String uas = c.getString(konfigurasi.TAG_UAS);
            String quiz = c.getString(konfigurasi.TAG_QUIZ);
            String tugas = c.getString(konfigurasi.TAG_TUGAS);
            String jmlchat = c.getString(konfigurasi.TAG_JMLCHAT);

            //editTextName.setText(name);
            editTextSkl.setText(desg);
            editTextKls.setText(kls);
            //Toast.makeText(getApplicationContext(), "hallooo.."+name, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt1:
                Txt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","1");
                        startActivity(i);
                    }
                });
            case R.id.txt2:
                Txt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","2");
                        startActivity(i);
                    }
                });
            case R.id.txt3:
                Txt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","3");
                        startActivity(i);
                    }
                });
            case R.id.txt4:
                Txt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","4");
                        startActivity(i);
                    }
                });
            case R.id.txt5:
                Txt5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","5");
                        startActivity(i);
                    }
                });
            case R.id.txt6:
                Txt6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","6");
                        startActivity(i);
                    }
                });
            case R.id.txt7:
                Txt7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","7");
                        startActivity(i);
                    }
                });
            case R.id.txt8:
                Txt8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","8");
                        startActivity(i);
                    }
                });
            case R.id.txt9:
                Txt9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //materi = Txt9.getText().toString();
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","9");
                        startActivity(i);
                    }
                });
            case R.id.txt10:
                Txt10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //materi = Txt9.getText().toString();
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","10");
                        startActivity(i);
                    }
                });
            case R.id.txt11:
                Txt11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","11");
                        startActivity(i);
                    }
                });
            case R.id.txt12:
                Txt12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MenuElearning.this, Elearning.class);
                        i.putExtra("materi","12");
                        startActivity(i);
                    }
                });

        }
    }
}
package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.projectbelajar.yuukbelajar.chat.activity.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView editTextName;
    private TextView editTextSkl;
    private ImageView Iklan, Iklan2;

    //private EditText eml;
    private String eml, nmx;
    private Integer jumlah;
    private Integer jumlah2;
    private Integer jumlah3;
    private Integer jumlah4;
    private Integer jumlah5;
    private Integer jumlah6;

    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private ImageView Image4;
    private ImageView Image5;
    private ImageView Image6;
    private ImageView Image7;
    private ImageView Image8;
    private ImageView Image9;
    //===================== info inbox
    private LinearLayout ly_notif;
    private LinearLayout ly_notif2;
    private LinearLayout ly_notif3;
    private LinearLayout ly_notif4;
    private LinearLayout ly_notif5;
    private LinearLayout ly_notif6;

    private TextView tv_notif;
    private TextView tv_notif2;
    private TextView tv_notif3;
    private TextView tv_notif4;
    private TextView tv_notif5;
    private TextView tv_notif6;

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    SessionManager sessionManager;

    private static final String pref_name = "crudpref";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        sliderView = (SliderView) findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);


        ImageView Iklan = (ImageView) findViewById(R.id.Iklan);
        Glide.with(CourseActivity.this).load("https://www.yuukbelajar.com/gbr/iklane1.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan);

        ImageView Iklan2 = (ImageView) findViewById(R.id.Iklan2);
        Glide.with(CourseActivity.this).load("https://www.yuukbelajar.com/gbr/iklane2.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan2);

        ImageView Iklan3 = (ImageView) findViewById(R.id.Iklan3);
        Glide.with(CourseActivity.this).load("https://www.yuukbelajar.com/gbr/iklane3.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan3);

        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextSkl = (TextView) findViewById(R.id.editTextSkl);

        Image1 = (ImageView) findViewById(R.id.image1);
        Image1.setOnClickListener(this);
        Image2 = (ImageView) findViewById(R.id.image2);
        Image2.setOnClickListener(this);
        Image3 = (ImageView) findViewById(R.id.image3);
        Image3.setOnClickListener(this);
        Image4 = (ImageView) findViewById(R.id.image4);
        Image4.setOnClickListener(this);
        Image5 = (ImageView) findViewById(R.id.image5);
        Image5.setOnClickListener(this);
        Image6 = (ImageView) findViewById(R.id.image6);
        Image6.setOnClickListener(this);
        Image7 = (ImageView) findViewById(R.id.image7);
        Image7.setOnClickListener(this);
        Image8 = (ImageView) findViewById(R.id.image8);
        Image8.setOnClickListener(this);
        Image9 = (ImageView) findViewById(R.id.image9);
        Image9.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);


        //sessionManager.logout();
        setupSlider();
        getEmployee();
        content();
    }

    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/iklan.jpg"));
        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image1.jpg"));
        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image2.jpg"));
        fragments.add(FragmentSlider.newInstance("https://yuukbelajar.com/gbr/image3.png"));

        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(this, mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();

    }



    private void getEmployee(){
        //Toast.makeText(getApplicationContext(), eml, Toast.LENGTH_LONG).show();
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CourseActivity.this,"","Wait...",false,false);
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
            String tingkat = c.getString(konfigurasi.TAG_TINGKAT);
            String sal = c.getString(konfigurasi.TAG_GAJIH);
            String uts = c.getString(konfigurasi.TAG_UTS);
            String uas = c.getString(konfigurasi.TAG_UAS);
            String quiz = c.getString(konfigurasi.TAG_QUIZ);
            String tugas = c.getString(konfigurasi.TAG_TUGAS);
            String jmlchat = c.getString(konfigurasi.TAG_JMLCHAT);
            String pesan = c.getString(konfigurasi.TAG_PESAN);

            editTextName.setText(name);
            editTextSkl.setText(desg);
            //Toast.makeText(getApplicationContext(), "hallooo.."+pesan, Toast.LENGTH_LONG).show();
            nmx = tingkat;

            jumlah = Integer.parseInt(sal);
            jumlah2 = Integer.parseInt(uts);
            jumlah3 = Integer.parseInt(uas);
            jumlah4 = Integer.parseInt(quiz);
            jumlah5 = Integer.parseInt(tugas);
            jumlah6 = Integer.parseInt(jmlchat);
            //        tmpilkan jumlah kedalam notifikasi
            tampilNotif(jumlah);
            tampilNotif2(jumlah2);
            tampilNotif3(jumlah3);
            tampilNotif4(jumlah4);
            tampilNotif5(jumlah5);
            tampilNotif6(jumlah6);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void content(){
        getEmployee();
        refresh (60000);
    }

    public void refresh(int milliseconds){
        final Handler handler = new Handler();
        final  Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    //========================================== imfo noif
    //android.support.design.widget.Toolbar
    private void tampilNotif(int i) {
        ly_notif = findViewById(R.id.ly_notif);
        tv_notif = findViewById(R.id.tv_notif);
        if (i>0){
            ly_notif.setVisibility(View.VISIBLE);
            tv_notif.setText(""+i);
        }else{
            ly_notif.setVisibility(View.GONE);
            tv_notif.setText(""+i);
        }
    }

    private void tampilNotif2(int i) {
        ly_notif2 = findViewById(R.id.ly_notif2);
        tv_notif2 = findViewById(R.id.tv_notif2);
        if (i>0){
            ly_notif2.setVisibility(View.VISIBLE);
            tv_notif2.setText(""+i);
        }else{
            ly_notif2.setVisibility(View.GONE);
            tv_notif2.setText(""+i);
        }
    }

    private void tampilNotif3(int i) {
        ly_notif3 = findViewById(R.id.ly_notif3);
        tv_notif3 = findViewById(R.id.tv_notif3);
        if (i>0){
            ly_notif3.setVisibility(View.VISIBLE);
            tv_notif3.setText(""+i);
        }else{
            ly_notif3.setVisibility(View.GONE);
            tv_notif3.setText(""+i);
        }
    }

    private void tampilNotif4(int i) {
        ly_notif4 = findViewById(R.id.ly_notif4);
        tv_notif4 = findViewById(R.id.tv_notif4);
        if (i>0){
            ly_notif4.setVisibility(View.VISIBLE);
            tv_notif4.setText(""+i);
        }else{
            ly_notif4.setVisibility(View.GONE);
            tv_notif4.setText(""+i);
        }
    }

    private void tampilNotif5(int i) {
        ly_notif5 = findViewById(R.id.ly_notif5);
        tv_notif5 = findViewById(R.id.tv_notif5);
        if (i>0){
            ly_notif5.setVisibility(View.VISIBLE);
            tv_notif5.setText(""+i);
        }else{
            ly_notif5.setVisibility(View.GONE);
            tv_notif5.setText(""+i);
        }
    }

    private void tampilNotif6(int i) {
        ly_notif6 = findViewById(R.id.ly_notif6);
        tv_notif6 = findViewById(R.id.tv_notif6);
        if (i>0){
            ly_notif6.setVisibility(View.VISIBLE);
            tv_notif6.setText(""+i);
        }else{
            ly_notif6.setVisibility(View.GONE);
            tv_notif6.setText(""+i);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image1:
                Image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, InfoSekolah.class);
                        startActivity(i);
                    }
                });
            case R.id.image2:
                Image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, InfoUts.class);
                        startActivity(i);
                    }
                });
            case R.id.image3:
                Image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, InfoUas.class);
                        startActivity(i);
                    }
                });
            case R.id.image4:
                Image4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, Harian.class);
                        startActivity(i);
                    }
                });
            case R.id.image5:
                Image5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, Perpus.class);
                        startActivity(i);
                    }
                });
            case R.id.image6:
                Image6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, WaliKls.class);
                        startActivity(i);
                    }
                });
            case R.id.image7:
                Image7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, ChatActivity.class);
                        startActivity(i);
                    }
                });
            case R.id.image8:
                Image8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CourseActivity.this, QuizOnline.class);
                        startActivity(i);
                    }
                });
            case R.id.image9:
                Image9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "hallooo.."+nmx, Toast.LENGTH_LONG).show();
                        if(nmx.equals("SD")){
                            Intent i = new Intent(CourseActivity.this, MenuElearningSd.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(CourseActivity.this, MenuElearning.class);
                            startActivity(i);
                        }
                    }
                });

        }
    }

}

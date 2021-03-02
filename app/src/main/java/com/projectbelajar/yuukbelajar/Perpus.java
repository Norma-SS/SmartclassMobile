package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class Perpus extends AppCompatActivity {
  private TextView editTextName;
  private TextView editTextSkl;
  private TextView editTextPesan;
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
    setContentView(R.layout.activity_perpus);

    sliderView = (SliderView) findViewById(R.id.sliderView);
    mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);


    ImageView Iklan = (ImageView) findViewById(R.id.Iklan);
    Glide.with(Perpus.this).load("https://www.yuukbelajar.com/gbr/iklane1.jpg")
            .fitCenter() //menyesuaikan ukuran imageview
            .crossFade() //animasi
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(Iklan);

    ImageView Iklan2 = (ImageView) findViewById(R.id.Iklan2);
    Glide.with(Perpus.this).load("https://www.yuukbelajar.com/gbr/iklane2.jpg")
            .fitCenter() //menyesuaikan ukuran imageview
            .crossFade() //animasi
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(Iklan2);

    ImageView Iklan3 = (ImageView) findViewById(R.id.Iklan3);
    Glide.with(Perpus.this).load("https://www.yuukbelajar.com/gbr/iklane3.jpg")
            .fitCenter() //menyesuaikan ukuran imageview
            .crossFade() //animasi
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(Iklan3);

    editTextName = (TextView) findViewById(R.id.editTextName);
    editTextSkl = (TextView) findViewById(R.id.editTextSkl);
    editTextPesan = (TextView) findViewById(R.id.editTextPesanx);

    sessionManager = new SessionManager(getApplicationContext());

    SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    eml = prefs.getString(password, null);

    //Toast.makeText(getApplicationContext(), "emailku.."+eml, Toast.LENGTH_LONG).show();
    //sessionManager.logout();
    setupSlider();
    getEmployee();
    //content();
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
        loading = ProgressDialog.show(Perpus.this,"","Wait...",false,false);
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
      String id = c.getString(konfigurasi.TAG_ID);
      String name = c.getString(konfigurasi.TAG_NAMA);
      String desg = c.getString(konfigurasi.TAG_POSISI);
      String pesan = c.getString(konfigurasi.TAG_PESAN);
      String tingkat = c.getString(konfigurasi.TAG_TINGKAT);
      String sal = c.getString(konfigurasi.TAG_GAJIH);
      String uts = c.getString(konfigurasi.TAG_UTS);
      String uas = c.getString(konfigurasi.TAG_UAS);
      String quiz = c.getString(konfigurasi.TAG_QUIZ);
      String tugas = c.getString(konfigurasi.TAG_TUGAS);
      String jmlchat = c.getString(konfigurasi.TAG_JMLCHAT);

      editTextName.setText(name);
      editTextSkl.setText(desg);
      editTextPesan.setText(pesan);
      //Toast.makeText(getApplicationContext(), "hallooo.."+pesan, Toast.LENGTH_LONG).show();
      nmx = new String(tingkat);



    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}

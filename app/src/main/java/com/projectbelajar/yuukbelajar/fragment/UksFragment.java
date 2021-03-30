package com.projectbelajar.yuukbelajar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.RequestHandler;
import com.projectbelajar.yuukbelajar.SessionManager;
import com.projectbelajar.yuukbelajar.adapter.ViewPagerAdapter;
import com.projectbelajar.yuukbelajar.chat.activity.ChatActivity;
import com.projectbelajar.yuukbelajar.konfigurasi;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class UksFragment extends Fragment implements View.OnClickListener {
    private TextView editTextName;

    //private EditText eml;
    private String eml, nmx;
    private Integer jumlah;
    private Integer jumlah2;
    private Integer jumlah3;

    private ImageView ChatDokter;
    private ImageView BukuSehat;
    private ImageView InfoSehat;
    //===================== info inbox
    private LinearLayout ly_notif;
    private LinearLayout ly_notif2;
    private LinearLayout ly_notif3;

    private TextView tv_notif;
    private TextView tv_notif2;
    private TextView tv_notif3;

    private Preferences preferences;

    private static ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<String> carouselBanners = new ArrayList<>();

    SessionManager sessionManager;

    private static final String pref_name = "crudpref";
    Context context;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int currentPos = 0;
    private int numpages = 0;

    public UksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new Preferences(getActivity());
        carouselBanners = new ArrayList<>();

        carouselBanners.add("https://www.yuukbelajar.com/gbr/iklan.jpg");
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image1.jpg");
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image2.jpg");
        carouselBanners.add("https://yuukbelajar.com/gbr/image3.png");

//        sliderView = view.findViewById(R.id.sliderUks);
//        mLinearLayout = view.findViewById(R.id.pagesContainer);


        ImageView Iklan = view.findViewById(R.id.Iklan);
        Glide.with(getActivity()).load("https://www.yuukbelajar.com/gbr/iklane1.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan);

        ImageView Iklan2 = view.findViewById(R.id.Iklan2);
        Glide.with(getActivity()).load("https://www.yuukbelajar.com/gbr/iklane2.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan2);

        ImageView Iklan3 = view.findViewById(R.id.Iklan3);
        Glide.with(getActivity()).load("https://image.freepik.com/free-vector/back-chool-template-wooden-board_1308-33554.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan3);

        viewPager = view.findViewById(R.id.sliderpager);
        DotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        adapter = new ViewPagerAdapter(carouselBanners, getActivity());
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        editTextName = view.findViewById(R.id.namasiswa);
        ChatDokter = view.findViewById(R.id.chatdokter);
        ChatDokter.setOnClickListener(this);
        BukuSehat = view.findViewById(R.id.bukusehat);
        BukuSehat.setOnClickListener(this);
        InfoSehat = view.findViewById(R.id.infosehat);
        InfoSehat.setOnClickListener(this);
        if (preferences.getValues("level").equals("DOKTER")){
            editTextName.setText(preferences.getValues( "klinik"));
        } else {
            editTextName.setText(preferences.getValues("namaSekolah"));
        }
        sessionManager = new SessionManager(getContext());

        SharedPreferences prefs = getActivity().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        //Toast.makeText(getApplicationContext(), "emailku.."+eml, Toast.LENGTH_LONG).show();
        //sessionManager.logout();
//        setupSlider();

        class GetEmployee extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
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

//                    editTextName.setText(name);
//                    editTextSkl.setText(desg);
                    //Toast.makeText(getApplicationContext(), "hallooo.."+pesan, Toast.LENGTH_LONG).show();
                    nmx = tingkat;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP, eml);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
        //content();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        autoRunCarousel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatdokter:
                ChatDokter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), "Menu Chat Dokter", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        preferences.setValues("chatType", "dokter");
//                        Toast.makeText(getActivity(), "chatType " + preferences.getValues("chatType"), Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                });
            case R.id.bukusehat:
                BukuSehat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Menu Buku Kesehatan", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(getActivity(), InfoUts.class);
//                        startActivity(i);
                    }
                });
            case R.id.infosehat:
                InfoSehat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Menu Info Kesehatan", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(getActivity(), InfoUas.class);
//                        startActivity(i);
                    }
                });
        }
    }

    private void autoRunCarousel() {


        NUM_PAGES = carouselBanners.size();
        numpages = carouselBanners.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                if (currentPos == numpages) {
                    currentPos = 0;
                }
                viewPager.setCurrentItem(currentPos++, true);
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        //delay in milliseconds before task is to be executed
        final long DELAY_MS = 500;
        // time in milliseconds between successive task executions
        final long PERIOD_MS = 5000;
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

}
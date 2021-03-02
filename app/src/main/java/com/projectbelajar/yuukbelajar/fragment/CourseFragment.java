package com.projectbelajar.yuukbelajar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projectbelajar.yuukbelajar.Harian;
import com.projectbelajar.yuukbelajar.InfoSekolah;
import com.projectbelajar.yuukbelajar.InfoUas;
import com.projectbelajar.yuukbelajar.InfoUts;
import com.projectbelajar.yuukbelajar.MenuElearning;
import com.projectbelajar.yuukbelajar.MenuElearningSd;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.QuizOnline;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.RequestHandler;
import com.projectbelajar.yuukbelajar.SessionManager;
import com.projectbelajar.yuukbelajar.WaliKls;
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

public class CourseFragment extends Fragment implements View.OnClickListener {
    private TextView editTextName;
    private TextView editTextSkl;

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
    private ImageView Image6;
    private ImageView Image7;
    private ImageView Image8;
    private ImageView Image9;
    private ImageView Image10;
    private ImageView Image11;
    private ImageView Image12;
    //===================== info inbox
    private LinearLayout ly_notif;
    private LinearLayout ly_notif2;
    private LinearLayout ly_notif3;
    private LinearLayout ly_notif4;
    private LinearLayout ly_notif6;

    private TextView tv_notif;
    private TextView tv_notif2;
    private TextView tv_notif3;
    private TextView tv_notif4;
    private TextView tv_notif6;


    private static ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<String> carouselBanners = new ArrayList<>();

    private Preferences preferences;
    SessionManager sessionManager;

    private static final String pref_name = "crudpref";
    Context context;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int currentPos = 0;
    private int numpages = 0;

    public CourseFragment() {
        // Required empty public constructor
    }

    private FirebaseUser firebaseUser;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);


        //sessionManager.logout();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new Preferences(getActivity());
        carouselBanners = new ArrayList<>();

        carouselBanners.add("https://www.yuukbelajar.com/gbr/iklan.jpg");
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image1.jpg");
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image2.jpg");
        carouselBanners.add("https://yuukbelajar.com/gbr/image3.png");

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
//        sliderView = view.findViewById(R.id.sliderCourse);
//        mLinearLayout = view.findViewById(R.id.pagesContainer);

        editTextName = view.findViewById(R.id.namasiswa);
        editTextSkl = view.findViewById(R.id.namasekolah);

        Image1 = view.findViewById(R.id.image1);
        Image1.setOnClickListener(this);
        Image2 = view.findViewById(R.id.image2);
        Image2.setOnClickListener(this);
        Image3 = view.findViewById(R.id.image3);
        Image3.setOnClickListener(this);
        Image4 = view.findViewById(R.id.image4);
        Image4.setOnClickListener(this);
        Image6 = view.findViewById(R.id.image6);
        Image6.setOnClickListener(this);
        Image7 = view.findViewById(R.id.image7);
        Image7.setOnClickListener(this);
        Image8 = view.findViewById(R.id.image8);
        Image8.setOnClickListener(this);
        Image9 = view.findViewById(R.id.image9);
        Image9.setOnClickListener(this);
        Image10 = view.findViewById(R.id.image10);
        Image10.setOnClickListener(this);
        Image11 = view.findViewById(R.id.image11);
        Image11.setOnClickListener(this);
        Image12 = view.findViewById(R.id.image12);
        Image12.setOnClickListener(this);

        editTextName.setText("Halo, " + preferences.getValues("nama"));
        editTextSkl.setText("Selamat Datang di " + preferences.getValues("namaSekolah"));

        sessionManager = new SessionManager(getContext());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

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

                    //Toast.makeText(getApplicationContext(), "hallooo.."+pesan, Toast.LENGTH_LONG).show();
                    nmx = new String(tingkat);

                    jumlah = Integer.parseInt(sal);
                    jumlah2 = Integer.parseInt(uts);
                    jumlah3 = Integer.parseInt(uas);
                    jumlah4 = Integer.parseInt(quiz);
                    jumlah5 = Integer.parseInt(tugas);
                    jumlah6 = Integer.parseInt(jmlchat);
                    //        tmpilkan jumlah kedalam notifikasi

                    ly_notif = view.findViewById(R.id.ly_notif);
                    tv_notif = view.findViewById(R.id.tv_notif);
                    if (jumlah > 0) {
                        ly_notif.setVisibility(View.VISIBLE);
                        tv_notif.setText("" + jumlah);
                    } else {
                        ly_notif.setVisibility(View.GONE);
                        tv_notif.setText("" + jumlah);
                    }
                    ly_notif2 = view.findViewById(R.id.ly_notif2);
                    tv_notif2 = view.findViewById(R.id.tv_notif2);
                    if (jumlah2 > 0) {
                        ly_notif2.setVisibility(View.VISIBLE);
                        tv_notif2.setText("" + jumlah2);
                    } else {
                        ly_notif2.setVisibility(View.GONE);
                        tv_notif2.setText("" + jumlah2);
                    }
                    ly_notif3 = view.findViewById(R.id.ly_notif3);
                    tv_notif3 = view.findViewById(R.id.tv_notif3);
                    if (jumlah3 > 0) {
                        ly_notif3.setVisibility(View.VISIBLE);
                        tv_notif3.setText("" + jumlah3);
                    } else {
                        ly_notif3.setVisibility(View.GONE);
                        tv_notif3.setText("" + jumlah3);
                    }
                    ly_notif4 = view.findViewById(R.id.ly_notif4);
                    tv_notif4 = view.findViewById(R.id.tv_notif4);
                    if (jumlah4 > 0) {
                        ly_notif4.setVisibility(View.VISIBLE);
                        tv_notif4.setText("" + jumlah4);
                    } else {
                        ly_notif4.setVisibility(View.GONE);
                        tv_notif4.setText("" + jumlah4);
                    }
                    ly_notif6 = view.findViewById(R.id.ly_notif6);
                    tv_notif6 = view.findViewById(R.id.tv_notif6);
                    if (jumlah6 > 0) {
                        ly_notif6.setVisibility(View.VISIBLE);
                        tv_notif6.setText("" + jumlah6);
                    } else {
                        ly_notif6.setVisibility(View.GONE);
                        tv_notif6.setText("" + jumlah6);
                    }
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        autoRunCarousel();
//        setupSlider();
    }
//
//    private void setupSlider() {
//        sliderView.setDurationScroll(1000);
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/iklan.jpg"));
//        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image1.jpg"));
//        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image2.jpg"));
//        fragments.add(FragmentSlider.newInstance("https://yuukbelajar.com/gbr/image3.png"));
//
//        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
//        sliderView.setAdapter(mAdapter);
//        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
//        mIndicator.setPageCount(fragments.size());
//        mIndicator.show();
//
//    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image1:
                Image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), InfoSekolah.class);
                        startActivity(i);
                    }
                });
            case R.id.image2:
                Image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), InfoUts.class);
                        startActivity(i);
                    }
                });
            case R.id.image3:
                Image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), InfoUas.class);
                        startActivity(i);
                    }
                });
            case R.id.image4:
                Image4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), Harian.class);
                        startActivity(i);
                    }
                });
            case R.id.image6:
                Image6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), WaliKls.class);
                        startActivity(i);
                    }
                });
            case R.id.image7:
                Image7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d("firebaseUser ", " "+firebaseUser);
                        if (firebaseUser == null) {
                            Toast.makeText(getActivity(), "Kamu belum login!", Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(getActivity(), "Kamu sudah login!", Toast.LENGTH_LONG).show();
                            if (preferences.getValues("level").equals("GURU")){
                                Toast.makeText(getActivity(), "Anda bukan walikelas!", Toast.LENGTH_LONG).show();
                            }else{
                                preferences.setValues("chatType", "walikelas");
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                startActivity(i);
                            }
//                            Toast.makeText(getActivity(), "chatType " + preferences.getValues("chatType"), Toast.LENGTH_SHORT).show();

//                            getActivity().finish();
                        }
                    }
                });
            case R.id.image8:
                Image8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), QuizOnline.class);
                        startActivity(i);
                    }
                });
            case R.id.image9:
                Image9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "hallooo.."+nmx, Toast.LENGTH_LONG).show();
                        if (nmx.equals("SD")) {
                            Intent i = new Intent(getActivity(), MenuElearningSd.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getActivity(), MenuElearning.class);
                            startActivity(i);
                        }
                    }
                });
            case R.id.image10:
                Image10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show();
                    }
                });
            case R.id.image11:
                Image11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show();
                    }
                });
            case R.id.image12:
                Image12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show();
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
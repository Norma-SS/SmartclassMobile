package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.projectbelajar.yuukbelajar.SessionManager.password;
public class UjianOnline extends AppCompatActivity {
    TextView txtNama, txtNo, txtWaktu, txtSoal, txtJml, txtId, txtJudul, txtNmskl, txtMapel, txtInduk;
    Button btnPrev, btnSelesai, btnNext;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4, rb5;
    ImageView img;
    EditText inputNama;
    int[] jawabanYgDiPilih = null;
    int[] jwb = null;
    String[] jb;
    int jwb1,jwb2,jwb3,jwb4,jwb5,jwb6,jwb7,jwb8,jwb9,jwb10;
    int jwb11,jwb12,jwb13,jwb14,jwb15,jwb16,jwb17,jwb18,jwb19,jwb20;
    int jwb21,jwb22,jwb23,jwb24,jwb25,jwb26,jwb27,jwb28,jwb29,jwb30;
    int jwb31,jwb32,jwb33,jwb34,jwb35,jwb36,jwb37,jwb38,jwb39,jwb40;
    int jwb41,jwb42,jwb43,jwb44,jwb45,jwb46,jwb47,jwb48,jwb49,jwb50;
    int[] jawabanYgBenar = null;
    boolean cekPertanyaan = false;
    int urutanPertanyaan = 0;
    List<Soal> listSoal;
    JSONArray soal = null;
    CounterClass mCountDownTimer;
    private ProgressDialog pDialog;

    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;
    private String eml, kdsoal,nmr,jwbb;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ujian_online);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        Intent intent = getIntent();
        kdsoal = intent.getStringExtra(konfigurasi.EMP_KDCHATT);
        nmr = intent.getStringExtra(konfigurasi.EMP_NOURUT);
        jwbb = intent.getStringExtra(konfigurasi.EMP_JWB);

        //int nmrx = Integer.parseInt(nmr);
        //Toast.makeText(getApplicationContext(), "nomor.."+nmrx+" ke "+jwbb, Toast.LENGTH_LONG).show();


        listSoal = new ArrayList<Soal>();

        txtJudul = (TextView) findViewById(R.id.judulquiz);
        txtNmskl = (TextView) findViewById(R.id.judulskl);
        txtMapel = (TextView) findViewById(R.id.kdmapel);
        txtId = (TextView) findViewById(R.id.textViewId);
        txtInduk = (TextView) findViewById(R.id.kdinduk);
        txtNama = (TextView) findViewById(R.id.textViewNama);
        txtNo = (TextView) findViewById(R.id.textViewNo);
        txtWaktu = (TextView) findViewById(R.id.textViewWaktu);
        txtJml = (TextView) findViewById(R.id.textViewJml);
        txtSoal = (TextView) findViewById(R.id.textViewSoal);
        btnPrev = (Button) findViewById(R.id.buttonPrev);
        btnSelesai = (Button) findViewById(R.id.buttonSelesai);
        btnNext = (Button) findViewById(R.id.buttonNext);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        rb1 = (RadioButton) findViewById(R.id.radio0);
        rb2 = (RadioButton) findViewById(R.id.radio1);
        rb3 = (RadioButton) findViewById(R.id.radio2);
        rb4 = (RadioButton) findViewById(R.id.radio3);
        //rb5 = (RadioButton) findViewById(R.id.radio4);
        FloatingActionButton floatingActionButton=findViewById(R.id.fab2);

        btnSelesai.setOnClickListener(klikSelesai);
        btnPrev.setOnClickListener(klikSebelum);
        btnNext.setOnClickListener(klikBerikut);
        floatingActionButton.setOnClickListener(klikCari);

        //floatingActionButton.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Floating Action Button Berhasil dibuat", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(UjianOnline.this, Kesoal.class);
                //startActivity(i);
            //}
        //});

        getJSON();
    }
    @Override
    public void onBackPressed() {
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_DAFTAR);
            Soal s = null;

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                s = new Soal();

                String id = jo.getString(konfigurasi.TAG_IDD);
                String soal = jo.getString(konfigurasi.TAG_SOAL);
                String a = jo.getString(konfigurasi.TAG_A);
                String b = jo.getString(konfigurasi.TAG_B);
                String c = jo.getString(konfigurasi.TAG_C);
                String d = jo.getString(konfigurasi.TAG_D);
                String jawaban = jo.getString(konfigurasi.TAG_JWB);
                String judul = jo.getString(konfigurasi.TAG_JDLUJI);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String mapel = jo.getString(konfigurasi.TAG_MAPEL);
                String induk = jo.getString(konfigurasi.TAG_ID);
                String nmssw = jo.getString(konfigurasi.TAG_NAMA);
                String tempoku = jo.getString(konfigurasi.TAG_TEMPO);
                String durasiku = jo.getString(konfigurasi.TAG_DURASI);

                //txtNo.setText(id);
                //txtSoal.setText(soal);
                //Toast.makeText(getApplicationContext(), "durasiku....."+durasiku, Toast.LENGTH_LONG).show();

                s.setId(id);
                s.setSoal(soal);
                s.setA(a);
                s.setB(b);
                s.setC(c);
                s.setD(d);
                s.setJawaban(jawaban);
                s.setJudul(judul);
                s.setNmskl(nmskl);
                s.setMapel(mapel);
                s.setInduk(induk);
                s.setNmssw(nmssw);
                s.setTempo(tempoku);
                s.setDurasi(durasiku);
                listSoal.add(s);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if (pDialog.isShowing())
        //pDialog.dismiss();

        jawabanYgDiPilih = new int[listSoal.size()];
        java.util.Arrays.fill(jawabanYgDiPilih, -1);
        jawabanYgBenar = new int[listSoal.size()];
        java.util.Arrays.fill(jawabanYgBenar, -1);
        setUpSoal();
    }

    private void getJSON() {

        final String kdsoalx = kdsoal;
        final String emlku = eml;

        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UjianOnline.this, "Loadig......", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_EMLX,emlku);
                params.put(konfigurasi.KEY_EMP_KDSOALX,kdsoalx);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_UJION2, params);
                return res;
            }


        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void setUpSoal() {
        //Collections.shuffle(listSoal);  mengacak soal
        setUpWaktu();
        tunjukanPertanyaan( 0, cekPertanyaan);
    }

    private void tunjukanPertanyaan(int urutan_soal_soal, boolean review) {
        btnSelesai.setEnabled(true);
        //if(urutan_soal_soal == 0)
            //setUpWaktu();

        try {
            rg.clearCheck();
            Soal soal = new Soal();
            soal = listSoal.get(urutan_soal_soal);

            //Toast.makeText(getApplicationContext(), "sdh tdk error ini bosss !!!", Toast.LENGTH_LONG).show();
            String soalnya = soal.getSoal();
            String idnya = soal.getId();
            String judulx = soal.getJudul();
            String nmsklx = soal.getNmskl();
            String mapelx = soal.getMapel();
            String indukx = soal.getInduk();
            String nmsswx = soal.getNmssw();
            String a = soal.getA();

            //txtId.setText(idnya);
            txtJudul.setText(judulx);
            txtNmskl.setText(nmsklx);
            txtMapel.setText(mapelx);
            txtInduk.setText(indukx);
            txtNama.setText(nmsswx);
            //txtJml.setText(listSoal.size()+" soal - "+soal.getTempo()+" menit");
            //Toast.makeText(getApplicationContext(), "isine soal ini boss....."+soalnya, Toast.LENGTH_LONG).show();
            txtId.setText("Kode Soal : " + idnya);
            txtSoal.setText(soalnya);
            rg.check(-1);
            rb1.setTextColor(Color.WHITE);
            rb2.setTextColor(Color.WHITE);
            rb3.setTextColor(Color.WHITE);
            rb4.setTextColor(Color.WHITE);
            //rb5.setTextColor(Color.WHITE);
            //imageLoader.DisplayImage(soal.getGambar(), img);
            rb1.setText("A. "+a);
            rb2.setText("B. "+soal.getB());
            rb3.setText("C. "+soal.getC());
            rb4.setText("D. "+soal.getD());
            //rb5.setText(soal.getJawaban());

            Log.d("", jawabanYgDiPilih[urutan_soal_soal] + "");
            if (jawabanYgDiPilih[urutan_soal_soal] == 1)
                rg.check(R.id.radio0);
            if (jawabanYgDiPilih[urutan_soal_soal] == 2)
                rg.check(R.id.radio1);
            if (jawabanYgDiPilih[urutan_soal_soal] == 3)
                rg.check(R.id.radio2);
            if (jawabanYgDiPilih[urutan_soal_soal] == 4)
                rg.check(R.id.radio3);

            //Log.d("", jawabanYgBenar[urutan_soal_soal] + "");
            //jawabanYgBenar[urutan_soal_soal] = soal.getJawaban();
            //soal.getJawaban()=Log.d("", Arrays.toString(jawabanYgBenar));


            pasangLabelDanNomorUrut();

            if (urutan_soal_soal == (listSoal.size() - 1)) {
                btnNext.setEnabled(false);
                btnSelesai.setEnabled(true);
            }

            if (urutan_soal_soal == 0)
                btnPrev.setEnabled(false);

            if (urutan_soal_soal > 0)
                btnPrev.setEnabled(true);

            if (urutan_soal_soal < (listSoal.size() - 1))
                btnNext.setEnabled(true);

            //Toast.makeText(getApplicationContext(), "yg ke-1 bos ", Toast.LENGTH_LONG).show();

            if (review) {
                mCountDownTimer.cancel();
                //Toast.makeText(getApplicationContext(), "yg ke-222222 bos ", Toast.LENGTH_LONG).show();
                Log.d("priksa", jawabanYgDiPilih[urutan_soal_soal] + ""
                        + jawabanYgBenar[urutan_soal_soal]);
                if (jawabanYgDiPilih[urutan_soal_soal] != jawabanYgBenar[urutan_soal_soal]) {
                    if (jawabanYgDiPilih[urutan_soal_soal] == 1)
                        rb1.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 2)
                        rb2.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 3)
                        rb3.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 4)
                        rb4.setTextColor(Color.RED);
                }
                if (jawabanYgBenar[urutan_soal_soal] == 1)
                    rb1.setTextColor(Color.WHITE);
                if (jawabanYgBenar[urutan_soal_soal] == 2)
                    rb2.setTextColor(Color.WHITE);
                if (jawabanYgBenar[urutan_soal_soal] == 3)
                    rb3.setTextColor(Color.WHITE);
                if (jawabanYgBenar[urutan_soal_soal] == 4)
                    rb4.setTextColor(Color.WHITE);
            }

        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
        }
    }

    private View.OnClickListener klikSelesai = new View.OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();
            Soal soal = new Soal();
            soal = listSoal.get(urutanPertanyaan);
            //String soalnya = soal.getSoal();
            String idnya = soal.getId();

            //Toast.makeText(getApplicationContext(), "sdh tdk error ini bosss !!!", Toast.LENGTH_LONG).show();
            //String soalnya = soal.getSoal();

            // hitung berapa yg benar
            //int jumlahJawabanYgBenar = 0; //(jawabanYgBenar[i] != -1) &&
            //for (int i = 0; i < jawabanYgDiPilih.length; i++) {
                //urutanPertanyaan = 0;
                //Toast.makeText(getApplicationContext(), "Jawaban PILIHAN No : "+i+" -> "+jawabanYgDiPilih[i], Toast.LENGTH_LONG).show();
            //}

            //for (int i = 0; i < jawabanYgBenar.length; i++) {
                //urutanPertanyaan = 0;
                //String jwbok = soal.getJawaban();
                //Toast.makeText(getApplicationContext(), "Jawaban BENAR ke : "+i+" -> "+jwbok , Toast.LENGTH_LONG).show();
            //}
            AlertDialog tampilKotakAlert;
            tampilKotakAlert = new AlertDialog.Builder(UjianOnline.this)
                    .create();
            tampilKotakAlert.setTitle("Mengakhiri Ujian Online");
            tampilKotakAlert.setIcon(R.drawable.ic_launcher);
            //tampilKotakAlert.setMessage("Score " + jumlahJawabanYgBenar * 10);

            tampilKotakAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "Batal",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            //mCountDownTimer.cancel();
                            //java.util.Arrays.fill(jawabanYgDiPilih, -1);
                            //cekPertanyaan = false;
                            //urutanPertanyaan = 0;
                            //tunjukanPertanyaan(0, cekPertanyaan);
                        }
                    });

            tampilKotakAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Keluar",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            mCountDownTimer.cancel();
                            cekPertanyaan = false;

                            kirim();
                            finish();
                        }
                    });

            tampilKotakAlert.show();

        }
    };

    private void kirim(){
        Soal soal = new Soal();
        soal = listSoal.get(urutanPertanyaan);
        final String idnya = soal.getId();

        //for (int i = 0; i <jawabanYgDiPilih.length; i++) {
            //jwb[i] = jawabanYgDiPilih[i];
            //Toast.makeText(getApplicationContext(), "Jawaban ke 1 : "+jawabanYgDiPilih[0] , Toast.LENGTH_LONG).show();
        //}

        if(listSoal.size()<=5) {
            jwb1 = jawabanYgDiPilih[0];
            jwb2 = jawabanYgDiPilih[1];
            jwb3 = jawabanYgDiPilih[2];
            jwb4 = jawabanYgDiPilih[3];
            jwb5 = jawabanYgDiPilih[4];
        } else if(listSoal.size()<=10) {
            jwb6 = jawabanYgDiPilih[5];
            jwb7 = jawabanYgDiPilih[6];
            jwb8 = jawabanYgDiPilih[7];
            jwb9 = jawabanYgDiPilih[8];
            jwb10 = jawabanYgDiPilih[9];
        } else if(listSoal.size()<=15) {
            jwb11 = jawabanYgDiPilih[10];
            jwb12 = jawabanYgDiPilih[11];
            jwb13 = jawabanYgDiPilih[12];
            jwb14 = jawabanYgDiPilih[13];
            jwb15 = jawabanYgDiPilih[14];
        }else if(listSoal.size()<=20) {
            jwb16 = jawabanYgDiPilih[15];
            jwb17 = jawabanYgDiPilih[16];
            jwb18 = jawabanYgDiPilih[17];
            jwb19 = jawabanYgDiPilih[18];
            jwb20 = jawabanYgDiPilih[19];
        }else if(listSoal.size()<=25) {
            jwb21 = jawabanYgDiPilih[20];
            jwb22 = jawabanYgDiPilih[21];
            jwb23 = jawabanYgDiPilih[22];
            jwb24 = jawabanYgDiPilih[23];
            jwb25 = jawabanYgDiPilih[24];
        }else if(listSoal.size()<=30) {
            jwb26 = jawabanYgDiPilih[25];
            jwb27 = jawabanYgDiPilih[26];
            jwb28 = jawabanYgDiPilih[27];
            jwb29 = jawabanYgDiPilih[28];
            jwb30 = jawabanYgDiPilih[29];
        }else if(listSoal.size()<=35) {
            jwb31 = jawabanYgDiPilih[30];
            jwb32 = jawabanYgDiPilih[31];
            jwb33 = jawabanYgDiPilih[32];
            jwb34 = jawabanYgDiPilih[33];
            jwb35 = jawabanYgDiPilih[34];
        }else if(listSoal.size()<=40) {
            jwb36 = jawabanYgDiPilih[35];
            jwb37 = jawabanYgDiPilih[36];
            jwb38 = jawabanYgDiPilih[37];
            jwb39 = jawabanYgDiPilih[38];
            jwb40 = jawabanYgDiPilih[39];
        }else if(listSoal.size()<=45) {
            jwb41 = jawabanYgDiPilih[40];
            jwb42 = jawabanYgDiPilih[41];
            jwb43 = jawabanYgDiPilih[42];
            jwb44 = jawabanYgDiPilih[43];
            jwb45 = jawabanYgDiPilih[44];
        }else if(listSoal.size()<=50) {
            jwb46 = jawabanYgDiPilih[45];
            jwb47 = jawabanYgDiPilih[46];
            jwb48 = jawabanYgDiPilih[47];
            jwb49 = jawabanYgDiPilih[48];
            jwb50 = jawabanYgDiPilih[49];
        }

        final String jb1 = Integer.toString(jwb1);
        final String jb2 = Integer.toString(jwb2);
        final String jb3 = Integer.toString(jwb3);
        final String jb4 = Integer.toString(jwb4);
        final String jb5 = Integer.toString(jwb5);
        final String jb6 = Integer.toString(jwb6);
        final String jb7 = Integer.toString(jwb7);
        final String jb8 = Integer.toString(jwb8);
        final String jb9 = Integer.toString(jwb9);
        final String jb10 = Integer.toString(jwb10);
        final String jb11 = Integer.toString(jwb11);
        final String jb12 = Integer.toString(jwb12);
        final String jb13 = Integer.toString(jwb13);
        final String jb14 = Integer.toString(jwb14);
        final String jb15 = Integer.toString(jwb15);
        final String jb16 = Integer.toString(jwb16);
        final String jb17 = Integer.toString(jwb17);
        final String jb18 = Integer.toString(jwb18);
        final String jb19 = Integer.toString(jwb19);
        final String jb20 = Integer.toString(jwb20);
        final String jb21 = Integer.toString(jwb21);
        final String jb22 = Integer.toString(jwb22);
        final String jb23 = Integer.toString(jwb23);
        final String jb24 = Integer.toString(jwb24);
        final String jb25 = Integer.toString(jwb25);
        final String jb26 = Integer.toString(jwb26);
        final String jb27 = Integer.toString(jwb27);
        final String jb28 = Integer.toString(jwb28);
        final String jb29 = Integer.toString(jwb29);
        final String jb30 = Integer.toString(jwb30);
        final String jb31 = Integer.toString(jwb31);
        final String jb32 = Integer.toString(jwb32);
        final String jb33 = Integer.toString(jwb33);
        final String jb34 = Integer.toString(jwb34);
        final String jb35 = Integer.toString(jwb35);
        final String jb36 = Integer.toString(jwb36);
        final String jb37 = Integer.toString(jwb37);
        final String jb38 = Integer.toString(jwb38);
        final String jb39 = Integer.toString(jwb39);
        final String jb40 = Integer.toString(jwb40);
        final String jb41 = Integer.toString(jwb41);
        final String jb42 = Integer.toString(jwb42);
        final String jb43 = Integer.toString(jwb43);
        final String jb44 = Integer.toString(jwb44);
        final String jb45 = Integer.toString(jwb45);
        final String jb46 = Integer.toString(jwb46);
        final String jb47 = Integer.toString(jwb47);
        final String jb48 = Integer.toString(jwb48);
        final String jb49 = Integer.toString(jwb49);
        final String jb50 = Integer.toString(jwb50);

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UjianOnline.this,"connect to server...","wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //showEmployee(s);
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                Intent i = new Intent(UjianOnline.this, CourseActivity.class);
                //i.putExtra(konfigurasi.EMP_,idnya);
                //i.putExtra(konfigurasi.EMP_ID,eml);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_IDSOAL,idnya);
                params.put(konfigurasi.KEY_EMP_EML,eml);

                    params.put(konfigurasi.KEY_EMP_1, jb1);
                    params.put(konfigurasi.KEY_EMP_2, jb2);
                    params.put(konfigurasi.KEY_EMP_3, jb3);
                    params.put(konfigurasi.KEY_EMP_4, jb4);
                    params.put(konfigurasi.KEY_EMP_5, jb5);
                    params.put(konfigurasi.KEY_EMP_6,jb6);
                    params.put(konfigurasi.KEY_EMP_7,jb7);
                    params.put(konfigurasi.KEY_EMP_8,jb8);
                    params.put(konfigurasi.KEY_EMP_9,jb9);
                    params.put(konfigurasi.KEY_EMP_10,jb10);
                    params.put(konfigurasi.KEY_EMP_11,jb11);
                    params.put(konfigurasi.KEY_EMP_12,jb12);
                    params.put(konfigurasi.KEY_EMP_13,jb13);
                    params.put(konfigurasi.KEY_EMP_14,jb14);
                    params.put(konfigurasi.KEY_EMP_15,jb15);
                    params.put(konfigurasi.KEY_EMP_16,jb16);
                    params.put(konfigurasi.KEY_EMP_17,jb17);
                    params.put(konfigurasi.KEY_EMP_18,jb18);
                    params.put(konfigurasi.KEY_EMP_19,jb19);
                    params.put(konfigurasi.KEY_EMP_20,jb20);
                    params.put(konfigurasi.KEY_EMP_21,jb21);
                    params.put(konfigurasi.KEY_EMP_22,jb22);
                    params.put(konfigurasi.KEY_EMP_23,jb23);
                    params.put(konfigurasi.KEY_EMP_24,jb24);
                    params.put(konfigurasi.KEY_EMP_25,jb25);
                    params.put(konfigurasi.KEY_EMP_26,jb26);
                    params.put(konfigurasi.KEY_EMP_27,jb27);
                    params.put(konfigurasi.KEY_EMP_28,jb28);
                    params.put(konfigurasi.KEY_EMP_29,jb29);
                    params.put(konfigurasi.KEY_EMP_30,jb30);

                    params.put(konfigurasi.KEY_EMP_31,jb31);
                    params.put(konfigurasi.KEY_EMP_32,jb32);
                    params.put(konfigurasi.KEY_EMP_33,jb33);
                    params.put(konfigurasi.KEY_EMP_34,jb34);
                    params.put(konfigurasi.KEY_EMP_35,jb35);
                    params.put(konfigurasi.KEY_EMP_36,jb36);
                    params.put(konfigurasi.KEY_EMP_37,jb37);
                    params.put(konfigurasi.KEY_EMP_38,jb38);
                    params.put(konfigurasi.KEY_EMP_39,jb39);
                    params.put(konfigurasi.KEY_EMP_40,jb40);

                    params.put(konfigurasi.KEY_EMP_41,jb41);
                    params.put(konfigurasi.KEY_EMP_42,jb42);
                    params.put(konfigurasi.KEY_EMP_43,jb43);
                    params.put(konfigurasi.KEY_EMP_44,jb44);
                    params.put(konfigurasi.KEY_EMP_45,jb45);
                    params.put(konfigurasi.KEY_EMP_46,jb46);
                    params.put(konfigurasi.KEY_EMP_47,jb47);
                    params.put(konfigurasi.KEY_EMP_48,jb48);
                    params.put(konfigurasi.KEY_EMP_49,jb49);
                    params.put(konfigurasi.KEY_EMP_50,jb50);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_UJION, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

    private View.OnClickListener klikSebelum = new View.OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();
            urutanPertanyaan--;
            if (urutanPertanyaan < 0)
                urutanPertanyaan = 0;

            tunjukanPertanyaan(urutanPertanyaan, cekPertanyaan);
        }
    };

    private View.OnClickListener klikCari = new View.OnClickListener() {
        public void onClick(View v) {
            lihat();
        }
    };

    private View.OnClickListener klikBerikut = new View.OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();
            urutanPertanyaan++;
            if (urutanPertanyaan >= listSoal.size())
                urutanPertanyaan = listSoal.size() - 1;

            tunjukanPertanyaan(urutanPertanyaan, cekPertanyaan);
        }
    };

    @SuppressLint("DefaultLocale")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Toast.makeText(getApplicationContext(), "Durasi waktu ujian sdh habis...!!!!! " , Toast.LENGTH_LONG).show();
            kirim();
            finish();
        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));
            txtWaktu.setText("Waktu Ujian : " + (hms));
        }
    }
    //========================================================================
    private void aturJawaban_nya() {
        if (rb1.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 1;
        if (rb2.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 2;
        if (rb3.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 3;
        if (rb4.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 4;

        Log.d("", Arrays.toString(jawabanYgDiPilih));
        Log.d("", Arrays.toString(jawabanYgBenar));

        //Toast.makeText(getApplicationContext(), "Jawaban nya boss adalah : "+jawabanYgDiPilih[urutanPertanyaan]+" jwban yg benar "+jawabanYgBenar[urutanPertanyaan], Toast.LENGTH_LONG).show();

    }

    private void setUpWaktu() {
        Soal soal = new Soal();
        soal = listSoal.get(urutanPertanyaan);
        String tmpx = soal.getDurasi();
        int tmp = Integer.parseInt(tmpx);
        //Toast.makeText(getApplicationContext(), "durasi : "+tmp, Toast.LENGTH_LONG).show();
        tmp = tmp * 60000;

        //Integer tmp = 120000;
        mCountDownTimer = new CounterClass(tmp, 1000);
        mCountDownTimer.start();
    }

    private void pasangLabelDanNomorUrut() {
        Soal soal = new Soal();
        soal = listSoal.get(urutanPertanyaan);

        txtJml.setText("Jumlah Soal - Durasi Ujian : " + listSoal.size()+" soal - "+soal.getTempo()+" menit");
        txtNo.setText("No. " + (urutanPertanyaan + 1)+ " ");//Jwbnya : "+jawabanYgDiPilih[urutanPertanyaan]+" ==>

    }

    private void lihat(){
        Soal soal = new Soal();
        soal = listSoal.get(urutanPertanyaan);
        final String idnya = soal.getId();

        //for (int i = 0; i <jawabanYgDiPilih.length; i++) {
        //jwb[i] = jawabanYgDiPilih[i];
        //Toast.makeText(getApplicationContext(), "Jawaban ke 1 : "+jawabanYgDiPilih[0] , Toast.LENGTH_LONG).show();
        //}

        if(listSoal.size()<=5) {
            jwb1 = jawabanYgDiPilih[0];
            jwb2 = jawabanYgDiPilih[1];
            jwb3 = jawabanYgDiPilih[2];
            jwb4 = jawabanYgDiPilih[3];
            jwb5 = jawabanYgDiPilih[4];
        } else if(listSoal.size()<=10) {
            jwb6 = jawabanYgDiPilih[5];
            jwb7 = jawabanYgDiPilih[6];
            jwb8 = jawabanYgDiPilih[7];
            jwb9 = jawabanYgDiPilih[8];
            jwb10 = jawabanYgDiPilih[9];
        } else if(listSoal.size()<=15) {
            jwb11 = jawabanYgDiPilih[10];
            jwb12 = jawabanYgDiPilih[11];
            jwb13 = jawabanYgDiPilih[12];
            jwb14 = jawabanYgDiPilih[13];
            jwb15 = jawabanYgDiPilih[14];
        }else if(listSoal.size()<=20) {
            jwb16 = jawabanYgDiPilih[15];
            jwb17 = jawabanYgDiPilih[16];
            jwb18 = jawabanYgDiPilih[17];
            jwb19 = jawabanYgDiPilih[18];
            jwb20 = jawabanYgDiPilih[19];
        }else if(listSoal.size()<=25) {
            jwb21 = jawabanYgDiPilih[20];
            jwb22 = jawabanYgDiPilih[21];
            jwb23 = jawabanYgDiPilih[22];
            jwb24 = jawabanYgDiPilih[23];
            jwb25 = jawabanYgDiPilih[24];
        }else if(listSoal.size()<=30) {
            jwb26 = jawabanYgDiPilih[25];
            jwb27 = jawabanYgDiPilih[26];
            jwb28 = jawabanYgDiPilih[27];
            jwb29 = jawabanYgDiPilih[28];
            jwb30 = jawabanYgDiPilih[29];
        }else if(listSoal.size()<=35) {
            jwb31 = jawabanYgDiPilih[30];
            jwb32 = jawabanYgDiPilih[31];
            jwb33 = jawabanYgDiPilih[32];
            jwb34 = jawabanYgDiPilih[33];
            jwb35 = jawabanYgDiPilih[34];
        }else if(listSoal.size()<=40) {
            jwb36 = jawabanYgDiPilih[35];
            jwb37 = jawabanYgDiPilih[36];
            jwb38 = jawabanYgDiPilih[37];
            jwb39 = jawabanYgDiPilih[38];
            jwb40 = jawabanYgDiPilih[39];
        }else if(listSoal.size()<=45) {
            jwb41 = jawabanYgDiPilih[40];
            jwb42 = jawabanYgDiPilih[41];
            jwb43 = jawabanYgDiPilih[42];
            jwb44 = jawabanYgDiPilih[43];
            jwb45 = jawabanYgDiPilih[44];
        }else if(listSoal.size()<=50) {
            jwb46 = jawabanYgDiPilih[45];
            jwb47 = jawabanYgDiPilih[46];
            jwb48 = jawabanYgDiPilih[47];
            jwb49 = jawabanYgDiPilih[48];
            jwb50 = jawabanYgDiPilih[49];
        }

        final String jb1 = Integer.toString(jwb1);
        final String jb2 = Integer.toString(jwb2);
        final String jb3 = Integer.toString(jwb3);
        final String jb4 = Integer.toString(jwb4);
        final String jb5 = Integer.toString(jwb5);
        final String jb6 = Integer.toString(jwb6);
        final String jb7 = Integer.toString(jwb7);
        final String jb8 = Integer.toString(jwb8);
        final String jb9 = Integer.toString(jwb9);
        final String jb10 = Integer.toString(jwb10);
        final String jb11 = Integer.toString(jwb11);
        final String jb12 = Integer.toString(jwb12);
        final String jb13 = Integer.toString(jwb13);
        final String jb14 = Integer.toString(jwb14);
        final String jb15 = Integer.toString(jwb15);
        final String jb16 = Integer.toString(jwb16);
        final String jb17 = Integer.toString(jwb17);
        final String jb18 = Integer.toString(jwb18);
        final String jb19 = Integer.toString(jwb19);
        final String jb20 = Integer.toString(jwb20);
        final String jb21 = Integer.toString(jwb21);
        final String jb22 = Integer.toString(jwb22);
        final String jb23 = Integer.toString(jwb23);
        final String jb24 = Integer.toString(jwb24);
        final String jb25 = Integer.toString(jwb25);
        final String jb26 = Integer.toString(jwb26);
        final String jb27 = Integer.toString(jwb27);
        final String jb28 = Integer.toString(jwb28);
        final String jb29 = Integer.toString(jwb29);
        final String jb30 = Integer.toString(jwb30);
        final String jb31 = Integer.toString(jwb31);
        final String jb32 = Integer.toString(jwb32);
        final String jb33 = Integer.toString(jwb33);
        final String jb34 = Integer.toString(jwb34);
        final String jb35 = Integer.toString(jwb35);
        final String jb36 = Integer.toString(jwb36);
        final String jb37 = Integer.toString(jwb37);
        final String jb38 = Integer.toString(jwb38);
        final String jb39 = Integer.toString(jwb39);
        final String jb40 = Integer.toString(jwb40);
        final String jb41 = Integer.toString(jwb41);
        final String jb42 = Integer.toString(jwb42);
        final String jb43 = Integer.toString(jwb43);
        final String jb44 = Integer.toString(jwb44);
        final String jb45 = Integer.toString(jwb45);
        final String jb46 = Integer.toString(jwb46);
        final String jb47 = Integer.toString(jwb47);
        final String jb48 = Integer.toString(jwb48);
        final String jb49 = Integer.toString(jwb49);
        final String jb50 = Integer.toString(jwb50);

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UjianOnline.this,"connect to server...","wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //showEmployee(s);
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                Intent i = new Intent(UjianOnline.this, LihatJwban.class);
                i.putExtra(konfigurasi.EMP_KDSOALE,idnya);
                //i.putExtra(konfigurasi.EMP_ID,eml);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_IDSOAL,idnya);
                params.put(konfigurasi.KEY_EMP_EML,eml);

                params.put(konfigurasi.KEY_EMP_1, jb1);
                params.put(konfigurasi.KEY_EMP_2, jb2);
                params.put(konfigurasi.KEY_EMP_3, jb3);
                params.put(konfigurasi.KEY_EMP_4, jb4);
                params.put(konfigurasi.KEY_EMP_5, jb5);
                params.put(konfigurasi.KEY_EMP_6,jb6);
                params.put(konfigurasi.KEY_EMP_7,jb7);
                params.put(konfigurasi.KEY_EMP_8,jb8);
                params.put(konfigurasi.KEY_EMP_9,jb9);
                params.put(konfigurasi.KEY_EMP_10,jb10);
                params.put(konfigurasi.KEY_EMP_11,jb11);
                params.put(konfigurasi.KEY_EMP_12,jb12);
                params.put(konfigurasi.KEY_EMP_13,jb13);
                params.put(konfigurasi.KEY_EMP_14,jb14);
                params.put(konfigurasi.KEY_EMP_15,jb15);
                params.put(konfigurasi.KEY_EMP_16,jb16);
                params.put(konfigurasi.KEY_EMP_17,jb17);
                params.put(konfigurasi.KEY_EMP_18,jb18);
                params.put(konfigurasi.KEY_EMP_19,jb19);
                params.put(konfigurasi.KEY_EMP_20,jb20);
                params.put(konfigurasi.KEY_EMP_21,jb21);
                params.put(konfigurasi.KEY_EMP_22,jb22);
                params.put(konfigurasi.KEY_EMP_23,jb23);
                params.put(konfigurasi.KEY_EMP_24,jb24);
                params.put(konfigurasi.KEY_EMP_25,jb25);
                params.put(konfigurasi.KEY_EMP_26,jb26);
                params.put(konfigurasi.KEY_EMP_27,jb27);
                params.put(konfigurasi.KEY_EMP_28,jb28);
                params.put(konfigurasi.KEY_EMP_29,jb29);
                params.put(konfigurasi.KEY_EMP_30,jb30);

                params.put(konfigurasi.KEY_EMP_31,jb31);
                params.put(konfigurasi.KEY_EMP_32,jb32);
                params.put(konfigurasi.KEY_EMP_33,jb33);
                params.put(konfigurasi.KEY_EMP_34,jb34);
                params.put(konfigurasi.KEY_EMP_35,jb35);
                params.put(konfigurasi.KEY_EMP_36,jb36);
                params.put(konfigurasi.KEY_EMP_37,jb37);
                params.put(konfigurasi.KEY_EMP_38,jb38);
                params.put(konfigurasi.KEY_EMP_39,jb39);
                params.put(konfigurasi.KEY_EMP_40,jb40);

                params.put(konfigurasi.KEY_EMP_41,jb41);
                params.put(konfigurasi.KEY_EMP_42,jb42);
                params.put(konfigurasi.KEY_EMP_43,jb43);
                params.put(konfigurasi.KEY_EMP_44,jb44);
                params.put(konfigurasi.KEY_EMP_45,jb45);
                params.put(konfigurasi.KEY_EMP_46,jb46);
                params.put(konfigurasi.KEY_EMP_47,jb47);
                params.put(konfigurasi.KEY_EMP_48,jb48);
                params.put(konfigurasi.KEY_EMP_49,jb49);
                params.put(konfigurasi.KEY_EMP_50,jb50);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_UJIONX, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

}

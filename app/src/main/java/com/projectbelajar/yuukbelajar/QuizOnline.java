package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;
import static java.nio.file.Paths.get;

public class QuizOnline extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView listView;
    private ImageView Ayoo;
    private String eml;
    private TextView NmSkl, Kelas, txtViewId, txtViewTitle, txtViewTgl, txtViewWkt;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;
    UjianOnline.CounterClass mCountDownTimer;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_online);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);
        //final CircleImageView Foto =  (CircleImageView) findViewById(R.id.Im);
        NmSkl = (TextView) findViewById(R.id.NmSkl);
        //Kelas = (TextView) findViewById(R.id.Kelas);

        //getting the recyclerview from xml
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        getJSON();
        //setUpWaktu();
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(konfigurasi.TAG_ID);
                String thajaran = jo.getString(konfigurasi.TAG_KETER2);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String tgl = jo.getString(konfigurasi.TAG_TGL);
                String wkt = jo.getString(konfigurasi.TAG_WKT);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String judul= jo.getString(konfigurasi.TAG_JUDUL);
                String link= jo.getString(konfigurasi.TAG_LINK);
                //String kode= jo.getString(konfigurasi.TAG_KODE);
                String tempo= jo.getString(konfigurasi.TAG_TEMPO);
                String mulai= jo.getString(konfigurasi.TAG_MULAI);
                String cek= jo.getString(konfigurasi.TAG_CEK);

                NmSkl.setText(nmskl);
                int xx = Integer.parseInt(mulai);
                if(xx > 0) {
                    Toast.makeText(getApplicationContext(), "Pelaksanaan Ujian Kurang : " + mulai + " menit lagi !!!", Toast.LENGTH_LONG).show();
                }
                //Kelas.setText(kls);
                //adding the product to product list

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_ID,id);
                employees.put(konfigurasi.TAG_KETER2,thajaran);
                employees.put(konfigurasi.TAG_KLS,kls);
                employees.put(konfigurasi.TAG_TGL,tgl);
                employees.put(konfigurasi.TAG_WKT,wkt);
                employees.put(konfigurasi.TAG_JUDUL,judul);
                employees.put(konfigurasi.TAG_LINK,link);
                //employees.put(konfigurasi.TAG_KODE,kode);
                employees.put(konfigurasi.TAG_TEMPO,tempo);
                employees.put(konfigurasi.TAG_MULAI,mulai);
                employees.put(konfigurasi.TAG_CEK,cek);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                QuizOnline.this, list, R.layout.ujion,

                new String[]{konfigurasi.TAG_JUDUL,konfigurasi.TAG_KETER2,konfigurasi.TAG_KLS,konfigurasi.TAG_TGL,konfigurasi.TAG_WKT,konfigurasi.TAG_TEMPO},
                new int[]{R.id.textViewId,R.id.textViewTitle,  R.id.textViewKls, R.id.textViewTgl, R.id.textViewWkt, R.id.textViewDurasi});

        listView.setAdapter(adapter);
    }


    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(QuizOnline.this, "Loadig......", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_QUIZON, eml);
//                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    //int xx = Integer.parseInt(mulai);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UjianOnline.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String kdchatt = map.get(konfigurasi.TAG_LINK);
        String mulai = map.get(konfigurasi.TAG_MULAI);
        String cek = map.get(konfigurasi.TAG_CEK);
        int xx = Integer.parseInt(mulai);
        int ck = Integer.parseInt(cek);

        if(xx <= 0) {   // && ck < 1
            intent.putExtra(konfigurasi.EMP_KDCHATT, kdchatt);
            //Toast.makeText(getApplicationContext(), empId, Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        if(ck > 0) {
            Toast.makeText(getApplicationContext(), "Anda sudah mengikuti ujian online untuk materi ini !!!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(QuizOnline.this, Tabbed.class);
            startActivity(i);
        }
        if(xx > 0) {
            Toast.makeText(getApplicationContext(), "Pelaksanaan Ujian Kurang : " + mulai + " menit lagi !!!", Toast.LENGTH_LONG).show();
        }
        return;
    }
}
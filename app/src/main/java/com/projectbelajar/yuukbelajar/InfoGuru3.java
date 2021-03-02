package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class InfoGuru3 extends AppCompatActivity{
    private ListView listView;
    private CircleImageView Foto;
    private ImageView Ayoo;
    private String eml, emlku;
    private TextView NmSkl, Kls, Keter, Mapel;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guru3);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        Keter = (TextView) findViewById(R.id.keter);
        NmSkl = (TextView) findViewById(R.id.nmskl);
        Kls = (TextView) findViewById(R.id.kls);
        Mapel = (TextView) findViewById(R.id.mapel);

        Intent intent = getIntent();
        emlku = intent.getStringExtra(konfigurasi.EMP_ID);

        listView = (ListView) findViewById(R.id.listView);
        //listView.setOnItemClickListener(this);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoGuru3.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_INFOGURU3, emlku);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String ket = jo.getString(konfigurasi.TAG_KET);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String mapel = jo.getString(konfigurasi.TAG_MAPEL);
                String no = jo.getString(konfigurasi.TAG_NO);
                String nama = jo.getString(konfigurasi.TAG_NAMA);
                String nil1 = jo.getString(konfigurasi.TAG_NIL1);
                String pre1 = jo.getString(konfigurasi.TAG_PRE1);
                String nil2 = jo.getString(konfigurasi.TAG_NIL2);
                String pre2 = jo.getString(konfigurasi.TAG_PRE2);

                Keter.setText(ket);
                NmSkl.setText(nmskl);
                Kls.setText(kls);
                Mapel.setText(mapel);
                //Toast.makeText(getApplicationContext(), "testing pesan "+nama+" ke-2 "+nil1, Toast.LENGTH_LONG).show();

                HashMap<String, String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_NO, no);
                employees.put(konfigurasi.TAG_NAMA, nama);
                employees.put(konfigurasi.TAG_NIL1, nil1);
                employees.put(konfigurasi.TAG_PRE1, pre1);
                employees.put(konfigurasi.TAG_NIL2, nil2);
                employees.put(konfigurasi.TAG_PRE2, pre2);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                InfoGuru3.this, list, R.layout.ruang_guru2,
                new String[]{konfigurasi.TAG_NO, konfigurasi.TAG_NAMA, konfigurasi.TAG_NIL1, konfigurasi.TAG_PRE1, konfigurasi.TAG_NIL2, konfigurasi.TAG_PRE2},
                new int[]{R.id.No, R.id.nmssw, R.id.nilp, R.id.pre1, R.id.nilk, R.id.pre2});

        listView.setAdapter(adapter);
    }


}
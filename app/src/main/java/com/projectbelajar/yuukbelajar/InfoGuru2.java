package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class InfoGuru2 extends AppCompatActivity  implements ListView.OnItemClickListener{
    private ListView listView;
    private CircleImageView Foto;
    private ImageView Ayoo;
    private String eml, emlku;
    private TextView NmSkl, NmGuru, txtViewId, txtViewTitle;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guru2);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        NmSkl = (TextView) findViewById(R.id.NmSkl);
        NmGuru = (TextView) findViewById(R.id.NmGuru);

        Intent intent = getIntent();
        emlku = intent.getStringExtra(konfigurasi.EMP_ID);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoGuru2.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_INFOGURU2, emlku);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String nip = jo.getString(konfigurasi.TAG_ID);
                String nama = jo.getString(konfigurasi.TAG_NAMA);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String no = jo.getString(konfigurasi.TAG_NO);
                String tgl = jo.getString(konfigurasi.TAG_TGL);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String mapel = jo.getString(konfigurasi.TAG_MAPEL);
                String kduji = jo.getString(konfigurasi.TAG_KDUJI);
                String trxuji = jo.getString(konfigurasi.TAG_TRXUJI);

                NmSkl.setText(nmskl);
                NmGuru.setText(nama);
                //adding the product to product list

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_ID,nip);
                employees.put(konfigurasi.TAG_NO,no);
                employees.put(konfigurasi.TAG_TGL,tgl);
                employees.put(konfigurasi.TAG_KLS,kls);
                employees.put(konfigurasi.TAG_MAPEL,mapel);
                employees.put(konfigurasi.TAG_KDUJI,kduji);
                employees.put(konfigurasi.TAG_TRXUJI,trxuji);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                InfoGuru2.this, list, R.layout.ruang_guru,
                new String[]{konfigurasi.TAG_NO,konfigurasi.TAG_TGL,konfigurasi.TAG_KLS,konfigurasi.TAG_MAPEL,konfigurasi.TAG_KDUJI},
                new int[]{R.id.No, R.id.tgl, R.id.kls, R.id.mapel, R.id.ket});

        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, InfoGuru3.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String nip = map.get(konfigurasi.TAG_ID);
        String trxuji = map.get(konfigurasi.TAG_TRXUJI);

        final String emlku = nip + "_" + trxuji + "_" + eml;
        intent.putExtra(konfigurasi.EMP_ID,emlku);
        //Toast.makeText(getApplicationContext(), empId, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
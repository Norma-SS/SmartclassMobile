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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class Elearning extends AppCompatActivity  implements ListView.OnItemClickListener {
    private ListView listView;
    private ImageView Ayoo;
    private String eml, emlx;
    private String KEY_NAME = "NAMA";
    private TextView NmSkl, Mapel, txtViewId, txtViewTitle, txtViewTgl, txtViewWkt;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elearning);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        emlx = prefs.getString(password, null);
        NmSkl = (TextView) findViewById(R.id.NmSkl);
        Mapel = (TextView) findViewById(R.id.mapel);
        //Intent i = getIntent();
        //String materi = i.getStringExtra(konfigurasi.EMP_MATERI);
        //Bundle extras= getIntent().getExtras();
        String materi = getIntent().getStringExtra("materi");
        eml = emlx + "_" + materi;
        //Toast.makeText(getApplicationContext(), "JUDUL MATERI "+eml, Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        getJSON();
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
                String walikls = jo.getString(konfigurasi.TAG_WALIKLS);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String tgl = jo.getString(konfigurasi.TAG_TGL);
                String wkt = jo.getString(konfigurasi.TAG_WKT);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String judul= jo.getString(konfigurasi.TAG_JUDUL);
                String link= jo.getString(konfigurasi.TAG_LINK);
                String nmmapel= jo.getString(konfigurasi.TAG_NMMAPEL);

                NmSkl.setText(nmskl);
                Mapel.setText(nmmapel);
                //Toast.makeText(getApplicationContext(), nmskl, Toast.LENGTH_LONG).show();
                //Kelas.setText(kls);
                //adding the product to product list

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_ID,id);
                employees.put(konfigurasi.TAG_WALIKLS,walikls);
                employees.put(konfigurasi.TAG_KLS,kls);
                employees.put(konfigurasi.TAG_TGL,tgl);
                employees.put(konfigurasi.TAG_WKT,wkt);
                employees.put(konfigurasi.TAG_JUDUL,judul);
                employees.put(konfigurasi.TAG_LINK,link);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                Elearning.this, list, R.layout.belon_list,

                new String[]{konfigurasi.TAG_JUDUL,konfigurasi.TAG_KLS,konfigurasi.TAG_TGL,konfigurasi.TAG_WKT},
                new int[]{R.id.textViewId,  R.id.textViewKls, R.id.textViewTgl, R.id.textViewWkt});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Elearning.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_BELON, eml);
                //String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Belajar.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId = map.get(konfigurasi.TAG_ID);
        String kdchatt = map.get(konfigurasi.TAG_LINK);

        intent.putExtra(konfigurasi.EMP_KDCHATT,kdchatt);
        //Toast.makeText(getApplicationContext(), empId, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

}
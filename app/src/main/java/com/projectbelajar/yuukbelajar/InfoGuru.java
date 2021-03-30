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

public class InfoGuru extends AppCompatActivity  implements ListView.OnItemClickListener{
    private ListView listView;
    private CircleImageView Foto;
    private ImageView Ayoo;
    private String eml;
    private TextView NmSkl, txtViewId, txtViewTitle;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guru);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);
        NmSkl = (TextView) findViewById(R.id.NmSkl);

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

                String no = jo.getString(konfigurasi.TAG_NO);
                String id = jo.getString(konfigurasi.TAG_ID);
                String nama = jo.getString(konfigurasi.TAG_NAMA);
                String email = jo.getString(konfigurasi.TAG_EMAIL);
                String level = jo.getString(konfigurasi.TAG_POSISI);
                String tg1 = jo.getString(konfigurasi.TAG_TGL);
                String wkt1 = jo.getString(konfigurasi.TAG_WKT);
                String tg2 = jo.getString(konfigurasi.TAG_TGL2);
                String wkt2 = jo.getString(konfigurasi.TAG_WKT2);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);


                NmSkl.setText(nmskl);
                //adding the product to product list

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_NO,no);
                employees.put(konfigurasi.TAG_ID,id);
                employees.put(konfigurasi.TAG_NAMA,nama);
                employees.put(konfigurasi.TAG_EMAIL,email);
                employees.put(konfigurasi.TAG_POSISI,level);
                employees.put(konfigurasi.TAG_TGL,tg1);
                employees.put(konfigurasi.TAG_WKT,wkt1);
                employees.put(konfigurasi.TAG_TGL2,tg1);
                employees.put(konfigurasi.TAG_WKT2,wkt2);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                InfoGuru.this, list, R.layout.infoguru,

                new String[]{konfigurasi.TAG_NAMA,konfigurasi.TAG_POSISI,konfigurasi.TAG_TGL},
                new int[]{R.id.nama, R.id.level, R.id.tg1});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoGuru.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_INFOGURU, eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, InfoGuru2.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String nip = map.get(konfigurasi.TAG_ID);

        final String emlku = nip + "_" + eml;
        intent.putExtra(konfigurasi.EMP_ID,emlku);
        //Toast.makeText(getApplicationContext(), empId, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
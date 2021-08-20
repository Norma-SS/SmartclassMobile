package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.projectbelajar.yuukbelajar.ui.ujianonline.UjianOnline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class LihatJwban extends AppCompatActivity  implements ListView.OnItemClickListener{
    private ListView listView;
    TextView txtNama, txtNo, txtWaktu, txtSoal, txtJml, txtId, txtJudul, txtNmskl, txtMapel, txtInduk;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;
    private String eml, kdsoal;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jwban);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        Intent intent = getIntent();
        kdsoal = intent.getStringExtra(konfigurasi.EMP_KDSOALE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        txtNmskl = (TextView) findViewById(R.id.judulskl);
        txtMapel = (TextView) findViewById(R.id.kdmapel);
        txtId = (TextView) findViewById(R.id.textViewId);
        txtInduk = (TextView) findViewById(R.id.kdinduk);
        txtNama = (TextView) findViewById(R.id.textViewNama);

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

                String kdsoale = jo.getString(konfigurasi.TAG_IDD);
                String idku = jo.getString(konfigurasi.TAG_INDUKE);
                String nourut = jo.getString(konfigurasi.TAG_NOURUT);
                String jwb = jo.getString(konfigurasi.TAG_JWB);
                String nama = jo.getString(konfigurasi.TAG_NAMEX);
                String mapel = jo.getString(konfigurasi.TAG_MAPELE);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKLE);


                //Toast.makeText(getApplicationContext(), "testing....."+nourut+" => "+jwb, Toast.LENGTH_LONG).show();
                txtNmskl.setText(nmskl);
                txtId.setText(kdsoale);
                txtMapel.setText(mapel);
                txtInduk.setText(idku);
                txtNama.setText(nama);

                //HashMap<String,String> params = new HashMap<>();
                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_IDD,kdsoale);
                employees.put(konfigurasi.TAG_NOURUT,nourut);
                employees.put(konfigurasi.TAG_JWB,jwb);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                LihatJwban.this, list, R.layout.list_jwban,
                new String[]{konfigurasi.TAG_NOURUT,konfigurasi.TAG_JWB},
                new int[]{R.id.no, R.id.jwb});

        listView.setAdapter(adapter);
    }

    private void getJSON() {

        final String kdsoalx = kdsoal;
        final String emlku = eml;

        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatJwban.this, "Loadig......", "Please wait...", false, false);
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
                String res = rh.sendPostRequest(konfigurasi.URL_JWBAN, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UjianOnline.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String kdsoale = map.get(konfigurasi.TAG_IDD);
        String nourut = map.get(konfigurasi.TAG_NOURUT);
        String jwb = map.get(konfigurasi.TAG_JWB);

        //Toast.makeText(getApplicationContext(), "menuju ujianol "+nourut, Toast.LENGTH_LONG).show();

        intent.putExtra(konfigurasi.EMP_KDCHATT,kdsoale);
        intent.putExtra(konfigurasi.EMP_NOURUT,nourut);
        intent.putExtra(konfigurasi.EMP_JWB,jwb);

        startActivity(intent);
    }
}
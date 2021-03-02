package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class Chatt2 extends AppCompatActivity implements View.OnClickListener{
    private TextView NamaWl;
    private TextView Status;
    private TextView Kls;
    private TextView Keter2;
    private CircleImageView Foto;
    private String eml, wlkls, kdchatt,emlku, pssx, emlx, emltujuan;
    private ListView listView2;
    private TextView Pesan;
    private Button Kirim;
    private String email2,pesan;
    private ProgressDialog pDialog;

    private static final String pref_name = "crudpref";
    //private String idku;
    SessionManager sessionManager;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //Progress dialog
    private ProgressDialog dialog;
    private String JSON_STRING;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt2);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        emlku = prefs.getString(password, null);

        NamaWl = (TextView) findViewById(R.id.NamaWl);
        Status = (TextView) findViewById(R.id.Status);
        Kls = (TextView) findViewById(R.id.Kls);
        Keter2 = (TextView) findViewById(R.id.Keter2);
        listView2 = (ListView) findViewById(R.id.listView2);
        Pesan = (TextView) findViewById(R.id.ePesan);
        Kirim = (Button) findViewById(R.id.eKirim);
        Kirim.setOnClickListener(this);

        Foto =  (CircleImageView) findViewById(R.id.Foto);

        Intent intent = getIntent();
        kdchatt = intent.getStringExtra(konfigurasi.EMP_KDCHATT);
        eml = intent.getStringExtra(konfigurasi.EMP_ID);
        //Toast.makeText(getApplicationContext(), kdchatt+" - "+eml, Toast.LENGTH_LONG).show();

        FloatingActionButton floatingActionButton=findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Floating Action Button Berhasil dibuat", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Chatt2.this, Tabbed.class);
                startActivity(i);
            }
        });

        getJSON();
        content();
    }

    public void content(){
        getJSON();
        refresh (30000);
    }

    public void refresh(int milliseconds){
        final Handler handler = new Handler();
        final  Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    @Override
    public void onBackPressed() {
    }

    private void addEmployee(){

        final String epesan = Pesan.getText().toString().trim();
        final String emlx = new String(emlku);
        final String emltujuan = new String(eml);
        //Toast.makeText(getApplicationContext(), kdchatt+" => "+eml, Toast.LENGTH_LONG).show();

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Chatt2.this,"send message...","wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //showEmployee(s);
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                Intent i = new Intent(Chatt2.this, Chatt22.class);
                i.putExtra(konfigurasi.EMP_KDCHATT,kdchatt);
                i.putExtra(konfigurasi.EMP_ID,eml);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_PESAN,epesan);
                params.put(konfigurasi.KEY_EMP_EMLX,emlx);
                params.put(konfigurasi.KEY_EMP_EMLTUJUAN,emltujuan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_PSNE, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }
    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Chatt2.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_TUJUANCHATT,eml);
                //Toast.makeText(getApplicationContext(), eml, Toast.LENGTH_LONG).show();
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
                String walikls = jo.getString(konfigurasi.TAG_WALIKLS);
                String stts = jo.getString(konfigurasi.TAG_STATUS);
                String foto = jo.getString(konfigurasi.TAG_FOTO);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String keter2 = jo.getString(konfigurasi.TAG_KETER2);
                String pesan = jo.getString(konfigurasi.TAG_PESAN);
                String pesan2 = jo.getString(konfigurasi.TAG_PESAN2);
                String tgl = jo.getString(konfigurasi.TAG_TGL);
                String wkt = jo.getString(konfigurasi.TAG_WKT);
                String tgl2 = jo.getString(konfigurasi.TAG_TGL2);
                String wkt2 = jo.getString(konfigurasi.TAG_WKT2);

                //Toast.makeText(getApplicationContext(), "testing pesan "+pesan+" ke-2 "+pesan2, Toast.LENGTH_LONG).show();
                NamaWl.setText(walikls);
                Status.setText(stts);
                Keter2.setText(keter2);
                Kls.setText(kls);

                Glide.with(this)
                        .load(foto)
                        .fitCenter() //menyesuaikan ukuran imageview
                        .crossFade() //animasi
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(Foto); //walikls.jpg

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_PESAN,pesan);
                employees.put(konfigurasi.TAG_PESAN2,pesan2);
                employees.put(konfigurasi.TAG_TGL,tgl);
                employees.put(konfigurasi.TAG_WKT,wkt);
                employees.put(konfigurasi.TAG_TGL2,tgl2);
                employees.put(konfigurasi.TAG_WKT2,wkt2);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Toast.makeText(getApplicationContext(), "nilaiku "+pssx , Toast.LENGTH_LONG).show();
        //if(pss.equals("1")){
        ListAdapter adapter = new SimpleAdapter(
                Chatt2.this, list, R.layout.list_chatt,
                new String[]{konfigurasi.TAG_PESAN,konfigurasi.TAG_TGL,konfigurasi.TAG_WKT,konfigurasi.TAG_PESAN2,konfigurasi.TAG_WKT2},
                new int[]{R.id.isichatt,R.id.tgljdl,R.id.wktchatt,R.id.isichatt2,R.id.wktchatt2});
        listView2.setAdapter(adapter);
        // }else{

    }

    @Override
    public void onClick(View v) {
        if (v == Kirim) {
            addEmployee();
        }
    }
}

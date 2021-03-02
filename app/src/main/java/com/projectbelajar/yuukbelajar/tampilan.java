package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class tampilan extends AppCompatActivity  implements View.OnClickListener{
    private String eml;

    private TextView Nama;
    private TextView Kls;
    private TextView Stts;
    private TextView Skl;
    private TextView Email;
    private TextView Hp;
    private CircleImageView Foto;

    private ImageView Edite;

    private String JSON_STRING;

    private static final String pref_name = "crudpref";
    Context context;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan);

        sessionManager = new SessionManager(getApplicationContext());

        sessionManager.getUserDetails();

        final CircleImageView Foto =  (CircleImageView) findViewById(R.id.Fotox);
        Nama = (TextView) findViewById(R.id.Namax);
        Stts = (TextView) findViewById(R.id.Sttsx);
        Kls =(TextView) findViewById(R.id.Klsx);
        Skl = (TextView) findViewById(R.id.Sklx);
        Email = (TextView) findViewById(R.id.Emailx);
        Hp = (TextView) findViewById(R.id.Hpx);

        Edite = (ImageView) findViewById(R.id.edite);
        Edite.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.logout();
            }
        });
        //=================================================================== getjson

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tampilan.this,"Loadig......","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //showEmployee();
                //================================================================ showemployee

                JSONObject jsonObject = null;
                ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);
                        String foto = jo.getString(konfigurasi.TAG_FOTO);
                        String nm = jo.getString(konfigurasi.TAG_NAMA);
                        String stts = jo.getString(konfigurasi.TAG_STATUS);
                        String kls = jo.getString(konfigurasi.TAG_KLS);
                        String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                        String email = jo.getString(konfigurasi.TAG_EMAIL);
                        String hp = jo.getString(konfigurasi.TAG_HP);

                        Nama.setText(nm);
                        Stts.setText(stts);
                        Kls.setText(kls);
                        Skl.setText(nmskl);
                        Email.setText(email);
                        Hp.setText(hp);

                        //Toast.makeText(getApplicationContext(), nm+" - "+foto, Toast.LENGTH_LONG).show();

                        Glide.with(tampilan.this).load(foto)
                                .fitCenter() //menyesuaikan ukuran imageview
                                .crossFade() //animasi
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(Foto); //walikls.jpg


                        //Toast.makeText(getApplicationContext(), foto, Toast.LENGTH_LONG).show();

                        HashMap<String,String> employees = new HashMap<>();
                        //employees.put(konfigurasi.TAG_TGL,tgl);
                        //employees.put(konfigurasi.TAG_KET,ket);
                        //employees.put(konfigurasi.TAG_MPEL,mpel);
                        //employees.put(konfigurasi.TAG_NIL,nil);

                        list.add(employees);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //=========================================================================
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_PROFIL,eml);
                //String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

        //getJSON();
    }

    @Override
    public void onBackPressed() {
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edite:
                Edite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(tampilan.this, UploadFoto.class);
                        startActivity(i);
                    }
                });
        }
    }

}

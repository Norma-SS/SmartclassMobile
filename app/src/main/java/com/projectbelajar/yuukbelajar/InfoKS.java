package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class InfoKS extends AppCompatActivity  implements View.OnClickListener{
    private static final String pref_name = "crudpref";
    //private String idku;
    SessionManager sessionManager;
    private ProgressDialog dialog;
    private String JSON_STRING;

    private String eml, wlkls, kdchatt,emlku, pssx, emlx, emltujuan;
    private TextView Pesan;
    private Button Kirim;
    private String email2,pesan;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_k_s);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);

        Pesan = (TextView) findViewById(R.id.pesan);
        Kirim = (Button) findViewById(R.id.kirim);
        Kirim.setOnClickListener(this);

        //getJSON();
    }

    @Override
    public void onClick(View v) {
        if (v == Kirim) {
            kirim();
        }
    }

    private void kirim(){

        final String epesan = Pesan.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoKS.this,"connecting to login...","wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //sessionManager.createSession(editTextEmail.getText().toString());
                showEmployee();
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                //hideDialog();

            }
            private void showEmployee(){
                JSONObject jsonObject = null;
                ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);
                        String ket1 = jo.getString(konfigurasi.TAG_KET);
                        String ket2 = jo.getString(konfigurasi.TAG_KETER2);

                        //Toast.makeText(InfoKS.this,"ket1 "+ket1+" ket2 "+ket2,Toast.LENGTH_LONG).show();

                        if(ket1.equals("SUKSES")){
                            Toast.makeText(getApplicationContext(), "KIRIM PESAN "+ket1, Toast.LENGTH_LONG).show();
                            Intent j = new Intent(InfoKS.this, AboutActivity.class);
                            startActivity(j);
                        } else{
                            Toast.makeText(InfoKS.this,"KIRIM PESAN "+ket1,Toast.LENGTH_LONG).show();
                            Toast.makeText(InfoKS.this,ket2,Toast.LENGTH_LONG).show();
                            //Intent j = new Intent(InfoKS.this, InfoKS.class);
                            //startActivity(j);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_PESAN,epesan);
                params.put(konfigurasi.KEY_EMP_EML,eml);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_INFOKS, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }
}
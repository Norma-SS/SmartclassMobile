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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;
public class WaliKls extends AppCompatActivity implements ListView.OnItemClickListener{
  private ListView listView;
  private String eml;

  private TextView Nama;
  private TextView Kls;

  private String JSON_STRING;

  private static final String pref_name = "crudpref";
  Context context;

  SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wali_kls);

    sessionManager = new SessionManager(getApplicationContext());

    Nama = (TextView) findViewById(R.id.Nama);
    Kls = (TextView) findViewById(R.id.Kls);

    listView = (ListView) findViewById(R.id.listView);
    //listView.setOnItemClickListener(this);

    SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    eml = prefs.getString(password, null);

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
        String tgl = jo.getString(konfigurasi.TAG_TGL);
        String ket = jo.getString(konfigurasi.TAG_KET);
        String nmssw = jo.getString(konfigurasi.TAG_NMSSW);
        String kls = jo.getString(konfigurasi.TAG_KLS);

        Nama.setText(nmssw);
        Kls.setText(kls);

        //Toast.makeText(getApplicationContext(), ket, Toast.LENGTH_LONG).show();

        HashMap<String,String> employees = new HashMap<>();
        employees.put(konfigurasi.TAG_TGL,tgl);
        employees.put(konfigurasi.TAG_KET,ket);

        list.add(employees);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    ListAdapter adapter = new SimpleAdapter(
            WaliKls.this, list, R.layout.list_info,
            new String[]{konfigurasi.TAG_TGL,konfigurasi.TAG_KET},
            new int[]{R.id.tgl, R.id.ket});

    listView.setAdapter(adapter);
  }

  private void getJSON(){
    class GetJSON extends AsyncTask<Void,Void,String> {

      ProgressDialog loading;
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(WaliKls.this,"Loadig......","Please wait...",false,false);
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
        String s = rh.sendGetRequestParam(konfigurasi.URL_GET_TUGAS,eml);
        //String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
        return s;
      }
    }
    GetJSON gj = new GetJSON();
    gj.execute();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent(this, tampilan.class);
    HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
    String empId = map.get(konfigurasi.TAG_TGL);
    //intent.putExtra(konfigurasi.EMP_ID,empId);
    startActivity(intent);
  }

}

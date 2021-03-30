package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class Chatting extends AppCompatActivity  implements ListView.OnItemClickListener{

    private ListView listView;
    private CircleImageView Foto;
    private ImageView Ayoo;
    private String eml;
    private TextView NmSkl, txtViewId, txtViewTitle;
    private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;
    SwipeRefreshLayout refreshLayout;

    SessionManager sessionManager;
    String imgurl="http://192.168.1.9/kis24/wlkls/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);
        //final CircleImageView Foto =  (CircleImageView) findViewById(R.id.Im);
        NmSkl = (TextView) findViewById(R.id.NmSkl);

        //getting the recyclerview from xml
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        Foto = (CircleImageView) findViewById(R.id.imageView);
        txtViewId = (TextView) findViewById(R.id.textViewId);
        txtViewTitle = (TextView) findViewById(R.id.textViewTitle);

        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
        R.color.colorAccent, R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        aksi();
                    }
                }, 1000);
            }
        });
        //this method will fetch and parse json
        //to display it in recyclerview
        //loadProducts();
        getJSON();
    }

    public void aksi(){
        //Toast.makeText(getApplicationContext(), "refresh sedang berlangsung..", Toast.LENGTH_LONG).show();
        //editTextSkl.setText("Refresh sedang beralan");
        //editTextSkl.setTextColor(Color.RED);
        getJSON();
        //refreshLayout.setRefreshing(false);
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
                String stts = jo.getString(konfigurasi.TAG_STATUS);
                String foto = jo.getString(konfigurasi.TAG_FOTO);
                String kls = jo.getString(konfigurasi.TAG_KLS);
                String keter2 = jo.getString(konfigurasi.TAG_KETER2);
                String tgl = jo.getString(konfigurasi.TAG_TGL);
                String wkt = jo.getString(konfigurasi.TAG_WKT);
                String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                String kdchat= jo.getString(konfigurasi.TAG_KDCHAT);

                NmSkl.setText(nmskl);
                //adding the product to product list

                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_ID,id);
                employees.put(konfigurasi.TAG_WALIKLS,walikls);
                employees.put(konfigurasi.TAG_STATUS,stts);
                employees.put(konfigurasi.TAG_FOTO,foto);
                employees.put(konfigurasi.TAG_KLS,kls);
                employees.put(konfigurasi.TAG_KETER2,keter2);
                employees.put(konfigurasi.TAG_TGL,tgl);
                employees.put(konfigurasi.TAG_WKT,wkt);
                employees.put(konfigurasi.TAG_KDCHAT,kdchat);
                list.add(employees);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
        Chatting.this, list, R.layout.product_list,

                new String[]{konfigurasi.TAG_WALIKLS,konfigurasi.TAG_STATUS,konfigurasi.TAG_KLS,konfigurasi.TAG_KETER2},
                new int[]{R.id.textViewId, R.id.textViewTitle, R.id.textViewKls, R.id.textViewKeter});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Chatting.this, "Loadig......", "Please wait...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_CHATT, eml);
                //String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Chatt.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId = map.get(konfigurasi.TAG_ID);
        String wlkls = map.get(konfigurasi.TAG_STATUS);
        String kdchatt = map.get(konfigurasi.TAG_KDCHAT);

        final String emlku = empId + "_" + eml;

        intent.putExtra(konfigurasi.EMP_ID,emlku);
        intent.putExtra(konfigurasi.EMP_WLKLS,wlkls);
        intent.putExtra(konfigurasi.EMP_KDCHATT,kdchatt);
        //Toast.makeText(getApplicationContext(), empId, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}

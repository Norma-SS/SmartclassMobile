package com.projectbelajar.yuukbelajar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Lupa extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextEmail;
    private TextView Pesan, Pesan2;
    private Button buttonLupa;
    private String JSON_STRING;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        Pesan = (TextView) findViewById(R.id.pesan);
        Pesan2 = (TextView) findViewById(R.id.pesan2);

        buttonLupa = (Button) findViewById(R.id.lupa);
        buttonLupa.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLupa) {
            if(editTextEmail.length() == 0){
                Toast.makeText(getApplicationContext(), "email dan password tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else if(isValidEmail(editTextEmail.getText().toString())){
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = editTextEmail.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                }
                            }
                        });
                addEmployee();
            } else {
                editTextEmail.setError("Format email salah");
                Toast.makeText(getApplicationContext(), "format email salah", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean isValidEmail(String email) {
        boolean validate;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            validate = true;
        } else if (email.matches(emailPattern2)) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }

    private void addEmployee(){

        final String eml = editTextEmail.getText().toString().trim();
        //Toast.makeText(getApplicationContext(), epesan, Toast.LENGTH_LONG).show();

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Lupa.this,"checking email...","wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                Intent i = new Intent(Lupa.this, Login.class);
                startActivity(i);
            }

            private void showEmployee(){
//                JSONObject jsonObject = null;
//                ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
//                    jsonObject = new JSONObject(JSON_STRING);
//                    JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
//
//                    for(int i = 0; i<result.length(); i++){
//                        JSONObject jo = result.getJSONObject(i);
//                        String ket1 = jo.getString(konfigurasi.TAG_KET);
//                        String ket2 = jo.getString(konfigurasi.TAG_KETER2);
//
//                        Toast.makeText(getApplicationContext(), ket1, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), ket2, Toast.LENGTH_LONG).show();
//
//                        Pesan.setText(ket1);
//                        Pesan2.setText(ket2);
//
//                    }


            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_EMAIL,eml);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_LUPA, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }


}
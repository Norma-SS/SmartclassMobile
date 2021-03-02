package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {
    //Defining views
    boolean doubleBackToExitPressedOnce = false;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Context context;
    private Button buttonLogin, buttonRegister, buttonLupa;
    private ProgressDialog pDialog;
    private TextView pesanReg, pesanLupa;
    private CheckBox ShowPass;

    private String JSON_STRING;

    SessionManager sessionManager;

    //firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Preferences preferences;

    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //firebase
//        FirebaseApp.initializeApp(this);
        preferences = new Preferences(this);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        sessionManager = new SessionManager(getApplicationContext());

        context = Login.this;

        //Initializing views
        pDialog = new ProgressDialog(context);
        editTextEmail = (EditText) findViewById(R.id.et_email);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        ShowPass = (CheckBox) findViewById(R.id.showPass);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        buttonLupa = (Button) findViewById(R.id.buttonLupa);
        buttonLupa.setOnClickListener(this);

        //pesanReg = (TextView) findViewById(R.id.btn_reg);
        //pesanLupa = (TextView) findViewById(R.id.btn_lupa);

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShowPass.isChecked()) {
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //Jika tidak, maka password akan di sembuyikan
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if (v == buttonLogin) {

            if (!email.equals("") && !password.equals("")) {
                login();
            } else {
                Toast.makeText(Login.this, "Email atau password tidak boleh kosong!", Toast.LENGTH_LONG).show();
            }
        } else if (v == buttonRegister) {
            Intent i = new Intent(Login.this, Registrasi.class);
            startActivity(i);
        } else if (v == buttonLupa) {
            Intent i = new Intent(Login.this, Lupa.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //=======================================================================================

    private void login() {


        //Toast.makeText(getApplicationContext(), stts1, Toast.LENGTH_LONG).show();

        class AddEmployee extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "connecting to login...", "wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                JSON_STRING = s;
//                sessionManager.createSession(editTextEmail.getText().toString());

                showEmployee();

                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                //hideDialog();

            }

            private void showEmployee() {
                JSONObject jsonObject = null;
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String ket1 = jo.getString(konfigurasi.TAG_KET);
                        String level = jo.getString(konfigurasi.TAG_POSISI);
                        Log.d("JO", " " + jo);
                        //Toast.makeText(Login.this,"ket "+ket1+" level "+level,Toast.LENGTH_LONG).show();


                        if (ket1.equals("SUKSES") && level.equals("KS")) {
                            Log.d("login ", " as " + level);
//                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, AboutActivity.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1.equals("SUKSES") && level.equals("SISWA")) {
                            Log.d("login ", " as " + level);
//                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1.equals("SUKSES") && (level.equals("ORANG TUA") || level.equals("WALI MURID"))) {
                            Log.d("login ", " as " + level);
//                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1.equals("SUKSES") && level.equals("WALIKELAS") || level.equals("GURU")) {
                            Log.d("login ", " as " + level);
//                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else {


                            //Intent j = new Intent(Login.this, Login.class);
                            //Toast.makeText(Login.this,"LOGIN "+ket1,Toast.LENGTH_LONG).show();
                        }

                        loginFireBase(email, password, level, loading);
//                        if (firebaseUser == null) {
//                            Log.d("Login Firebase", " ok");
//                        } else {
//                            Log.d("Logged Firebase", " " + firebaseUser.getEmail());
//                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_EMAIL, email);
                params.put(konfigurasi.KEY_EMP_PASSWORD, password);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_LOGIN, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    //===============================================================================================

    private void loginFireBase(final String email, final String password, final String level, final ProgressDialog loading) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reference.child(auth.getCurrentUser().getUid()).child("email").setValue(email);
                            reference.child(auth.getCurrentUser().getUid()).child("password").setValue(password);
                            reference.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    loading.dismiss();
                                    String name = (String) dataSnapshot.child("nama").getValue();
                                    String level1 = (String) dataSnapshot.child("level").getValue();
                                    String kodesekolah = (String) dataSnapshot.child("kodeSekolah").getValue();
                                    String nis = (String) dataSnapshot.child("nis").getValue();
                                    String nip = (String) dataSnapshot.child("nip").getValue();
                                    String foto = (String) dataSnapshot.child("imgUrl").getValue();
                                    String password = (String) dataSnapshot.child("password").getValue();
                                    String nohp = (String) dataSnapshot.child("username").getValue();
                                    String email = (String) dataSnapshot.child("email").getValue();
                                    String namaSekolah = (String) dataSnapshot.child("namaSekolah").getValue();
                                    String kelas = (String) dataSnapshot.child("kelas").getValue();
                                    if (level1.equals("DOKTER")) {
                                        String klinik = (String) dataSnapshot.child("klinik").getValue();
                                        preferences.setValues("klinik", klinik);
                                    }



//                                    String kodesekolahdokter =(String) dataSnapshot.child("kodesekolah").getValue();




                                    preferences.setValues("hp", nohp);
                                    preferences.setValues("password", password);
                                    preferences.setValues("email", email);
                                    preferences.setValues("nama", name);
                                    preferences.setValues("level", level1);
                                    preferences.setValues("kodesekolah", kodesekolah);
                                    preferences.setValues("nis", nis);
                                    preferences.setValues("nip", nip);
                                    preferences.setValues("namaSekolah", namaSekolah);
                                    preferences.setValues("kelas", kelas);
                                    preferences.setValues("foto", foto);

                                    Log.d("LOGIN Firebase ", "Data success " + preferences.getValues("nama") + " " + kodesekolah);
                                    sessionManager.createSession(editTextEmail.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Berhasil login!", Toast.LENGTH_SHORT).show();

//                                    Intent j = new Intent(Login.this, NewActivity.class);
//                                    startActivity(j);
//                                    finish();

                                    switch (preferences.getValues("level")) {
                                        case "DOKTER": {
                                            Intent j = new Intent(Login.this, TabbedDoktor.class);
//                                            Intent j = new Intent(Login.this, NewLayoutActivity.class);
                                            startActivity(j);
                                            finish();
                                            break;
                                        }
                                        case "KS": {
                                            Intent j = new Intent(Login.this, TabbedKepsek.class);
                                            startActivity(j);
                                            finish();
                                            break;
                                        }
                                        case "GURU":
                                        case "WALIKELAS": {
                                            Intent j = new Intent(Login.this, TabbedGuru.class);
                                            startActivity(j);
                                            finish();
                                            break;
                                        }
                                        default: {
                                            Intent j = new Intent(Login.this, Tabbed.class);
                                            startActivity(j);
                                            finish();
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    loading.dismiss();
                                    Log.d("cancelled ", " true ");
                                }
                            });
//                            circularProgressBar.setVisibility(View.GONE);
//                            bgLoad.setVisibility(View.GONE);
//                            Intent intent = new Intent(Login.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            Toast.makeText(Login.this, "Login Success -C", Toast.LENGTH_SHORT).show();
//                            Log.d("LOGIN Firebase ", " success " + email + " " + level);


//                            j.putExtra("level", "DOKTER");
                        } else {
//                            circularProgressBar.setVisibility(View.GONE);
//                            bgLoad.setVisibility(View.GONE);
                            Log.d("LOGIN Firebase ", " failed");
                            Toast.makeText(getApplicationContext(), "Gagal login!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
//                            Toast.makeText(Login.this, "Akun belum terdaftar, Silahkan daftar terlebih dahulu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
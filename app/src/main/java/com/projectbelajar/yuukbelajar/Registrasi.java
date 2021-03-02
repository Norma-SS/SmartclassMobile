package com.projectbelajar.yuukbelajar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Registrasi extends AppCompatActivity implements View.OnClickListener {
    //Defining views
    private EditText editTextKdSkl;
    private EditText editTextId;
    private EditText editTextNama;
    private EditText stts;
    private EditText editTextEmail;
    private EditText editTextHp;
    private EditText editTextPassword;
    //private Context context;
    private Button buttonRegister;
    private CheckBox ShowPass;

    private Spinner spinner;

    private String JSON_STRING;

    //firebase
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        preferences = new Preferences(this);

        //firebase
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        spinner = (Spinner) findViewById(R.id.stts);
        spinner.setOnItemSelectedListener(new ItemSelectedListener());


        //Inisialisasi dari View
        editTextKdSkl = (EditText) findViewById(R.id.editTextKdSkl);
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNama = (EditText) findViewById(R.id.editTextnama);
        editTextHp = (EditText) findViewById(R.id.editTextHp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        ShowPass = (CheckBox) findViewById(R.id.showPass);

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

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
    }


    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void addEmployee() {

        final String kdskl = editTextKdSkl.getText().toString().trim();
        final String id = editTextId.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String stts2 = spinner.getSelectedItem().toString().toUpperCase();
        final String hp = editTextHp.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Toast.makeText(getApplicationContext(), stts1, Toast.LENGTH_LONG).show();

        class AddEmployee extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Registrasi.this, "Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
//                Intent i = new Intent(Registrasi.this, Login.class);
//                startActivity(i);
                Log.d("status", stts2);
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
                        String ket2 = jo.getString(konfigurasi.TAG_KETER2);
                        String namaSekolah = jo.getString("nmskl");
                        String kelas = jo.getString("kls");

//                        Toast.makeText(getApplicationContext(), ket1, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), ket2, Toast.LENGTH_LONG).show();

                        Log.d("ket 1 ", ket1);
                        Log.d("ket 2 ", ket2);
                        if (ket2.equals("Terima Kasih")){
                            registerFirebase(hp, email, password, stts2, kdskl, id, nama, namaSekolah, kelas);
                        }else{
                            Toast.makeText(getApplicationContext(), "Cek kembali data anda atau hubungi admin!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_KDSKL, kdskl);
                params.put(konfigurasi.KEY_EMP_ID, id);
                params.put(konfigurasi.KEY_EMP_NAMA, nama);
                params.put(konfigurasi.KEY_EMP_STTS, stts2);
                params.put(konfigurasi.KEY_EMP_HP, hp);
                params.put(konfigurasi.KEY_EMP_EMAIL, email);
                params.put(konfigurasi.KEY_EMP_PASSWORD, password);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();


    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            if (editTextNama.length() == 0) {
                Toast.makeText(getApplicationContext(), "nama tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else if (editTextEmail.length() == 0 && editTextPassword.length() == 0) {
                Toast.makeText(getApplicationContext(), "email dan password tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else if (spinner.getSelectedItem().toString().toUpperCase().equals("PILIH STATUS")) {
                Toast.makeText(getApplicationContext(), "Status belum dipilih", Toast.LENGTH_LONG).show();
            } else if (isValidEmail(editTextEmail.getText().toString())) {
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

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String stts = String.valueOf(spinner.getSelectedItem());


        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            final String stts1 = spinner.getSelectedItem().toString();
            if (stts.equals(String.valueOf(spinner.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.getContext(),
                        "Anda memilih : " + stts1,
                        Toast.LENGTH_LONG).show();
                // Todo when item is selected by the user
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

    }

    private void registerFirebase(final String username, final String email, final String password, final String level, final String kodeSekolah, final String nis, final String nama, final String namaSekolah, final String kelas) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(Registrasi.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("level", level);
                            hashMap.put("kodeSekolah", kodeSekolah);
                            hashMap.put("nis", nis);
                            hashMap.put("nama", nama);
                            hashMap.put("sort_nama", nama.toLowerCase());
                            hashMap.put("email", email);
                            hashMap.put("password", password);
                            hashMap.put("namaSekolah", namaSekolah);
                            hashMap.put("kelas", kelas);
                            hashMap.put("status", "offline");
                            hashMap.put("imgUrl", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registrasi.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                        preferences.setValues( "nama", nama );
                                        preferences.setValues( "level", level );
                                        preferences.setValues( "kodesekolah", kodeSekolah );
                                        preferences.setValues("nis", nis );
                                        preferences.setValues("foto", "default" );
//                                        circularProgressBar.setVisibility(View.GONE);
//                                        bgLoad.setVisibility(View.GONE);
                                        Intent intent = new Intent(Registrasi.this, Login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Registrasi.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Registrasi.this, "Registrasi gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
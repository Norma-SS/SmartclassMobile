package com.projectbelajar.yuukbelajar

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Login : AppCompatActivity(), View.OnClickListener {
    //Defining views
    var doubleBackToExitPressedOnce = false
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var context: Context? = null
    private var buttonLogin: Button? = null
    private var buttonRegister: Button? = null
    private var buttonLupa: Button? = null
    private var pDialog: ProgressDialog? = null
    private val pesanReg: TextView? = null
    private val pesanLupa: TextView? = null
    private var ShowPass: CheckBox? = null
    private var JSON_STRING: String? = null
    var sessionManager: SessionManager? = null

    //firebase
    private var firebaseUser: FirebaseUser? = null
    private var auth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null
    private var preferences: Preferences? = null
    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //firebase
//        FirebaseApp.initializeApp(this);
        preferences = Preferences(this)
        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Users")
        sessionManager = SessionManager(applicationContext)
        context = this@Login

        //Initializing views
        pDialog = ProgressDialog(context)
        editTextEmail = findViewById<View>(R.id.et_email) as EditText
        editTextPassword = findViewById<View>(R.id.et_password) as EditText
        ShowPass = findViewById<View>(R.id.showPass) as CheckBox
        buttonLogin = findViewById<View>(R.id.buttonLogin) as Button
        buttonLogin!!.setOnClickListener(this)
        buttonRegister = findViewById<View>(R.id.buttonRegister) as Button
        buttonRegister!!.setOnClickListener(this)
        buttonLupa = findViewById<View>(R.id.buttonLupa) as Button
        buttonLupa!!.setOnClickListener(this)

        //pesanReg = (TextView) findViewById(R.id.btn_reg);
        //pesanLupa = (TextView) findViewById(R.id.btn_lupa);
        email = editTextEmail!!.text.toString().trim { it <= ' ' }
        password = editTextPassword!!.text.toString().trim { it <= ' ' }
        ShowPass!!.setOnClickListener {
            if (ShowPass!!.isChecked) {
                //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                editTextPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                //Jika tidak, maka password akan di sembuyikan
                editTextPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun onClick(v: View) {
        email = editTextEmail!!.text.toString().trim { it <= ' ' }
        password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (v === buttonLogin) {
            if (email != "" && password != "") {
                login()
            } else {
                Toast.makeText(this@Login, "Email atau password tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }
        } else if (v === buttonRegister) {
            val i = Intent(this@Login, Registrasi::class.java)
            startActivity(i)
        } else if (v === buttonLupa) {
            val i = Intent(this@Login, Lupa::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    //=======================================================================================
    private fun login() {


        //Toast.makeText(getApplicationContext(), stts1, Toast.LENGTH_LONG).show();
        class AddEmployee : AsyncTask<Void?, Void?, String?>() {
            var loading: ProgressDialog? = null
            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(this@Login, "connecting to login...", "wait...", false, false)
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                //                loading.dismiss();
                JSON_STRING = s
                //                sessionManager.createSession(editTextEmail.getText().toString());
                showEmployee()

                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                //hideDialog();
            }

            private fun showEmployee() {
                var jsonObject: JSONObject? = null
                val list = ArrayList<HashMap<String, String>>()
                try {
                    jsonObject = JSONObject(JSON_STRING)
                    val result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY)
                    for (i in 0 until result.length()) {
                        val jo = result.getJSONObject(i)
                        val ket1 = jo.getString(konfigurasi.TAG_KET)
                        val level = jo.getString(konfigurasi.TAG_POSISI)
                        Log.d("JO", " $jo")
                        //Toast.makeText(Login.this,"ket "+ket1+" level "+level,Toast.LENGTH_LONG).show();
                        if (ket1 == "SUKSES" && level == "KS") {
                            Log.d("login ", " as $level")
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, AboutActivity.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "SUKSES" && level == "SISWA") {
                            Log.d("login ", " as $level")
                            loginFireBase(email, password, level, loading)

                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "SUKSES" && (level == "ORANG TUA" || level == "WALI MURID")) {
                            Log.d("login ", " as $level")
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "SUKSES" && level == "WALIKELAS" || level == "GURU") {
                            Log.d("login ", " as $level")
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else {


                            //Intent j = new Intent(Login.this, Login.class);
                            //Toast.makeText(Login.this,"LOGIN "+ket1,Toast.LENGTH_LONG).show();
                        }
//                        loginFireBase(email, password, level, loading)
                        //                        if (firebaseUser == null) {
//                            Log.d("Login Firebase", " ok");
//                        } else {
//                            Log.d("Logged Firebase", " " + firebaseUser.getEmail());
//                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            protected override fun doInBackground(vararg params: Void?): String? {
                val params = HashMap<String, String?>()
                params[konfigurasi.KEY_EMP_EMAIL] = email
                params[konfigurasi.KEY_EMP_PASSWORD] = password
                val rh = RequestHandler()
                return rh.sendPostRequest(konfigurasi.URL_LOGIN, params)
            }
        }

        val ae = AddEmployee()
        ae.execute()
    }

    //===============================================================================================
    private fun loginFireBase(email: String?, password: String?, level: String, loading: ProgressDialog?) {
        auth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference!!.child(auth!!.currentUser!!.uid).child("email").setValue(email)
                        reference!!.child(auth!!.currentUser!!.uid).child("password").setValue(password)
                        reference!!.child(auth!!.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                loading!!.dismiss()
                                val name = dataSnapshot.child("nama").value as String?
                                val level1 = dataSnapshot.child("level").value as String?
                                val kodesekolah = dataSnapshot.child("kodeSekolah").value as String?
                                val nis = dataSnapshot.child("nis").value as String?
                                val nip = dataSnapshot.child("nip").value as String?
                                val foto = dataSnapshot.child("imgUrl").value as String?
                                val password = dataSnapshot.child("password").value as String?
                                val nohp = dataSnapshot.child("username").value as String?
                                val email = dataSnapshot.child("email").value as String?
                                val namaSekolah = dataSnapshot.child("namaSekolah").value as String?
                                val kelas = dataSnapshot.child("kelas").value as String?
                                //                                    if (level1.equals("DOKTER")) {
//                                        String klinik = (String) dataSnapshot.child("klinik").getValue();
//                                        preferences.setValues("klinik", klinik);
//                                    }


//                                    String kodesekolahdokter =(String) dataSnapshot.child("kodesekolah").getValue();
                                preferences!!.setValues("hp", nohp)
                                preferences!!.setValues("password", password)
                                preferences!!.setValues("email", email)
                                preferences!!.setValues("nama", name)
                                preferences!!.setValues("level", level)
                                preferences!!.setValues("kodesekolah", kodesekolah)
                                preferences!!.setValues("nis", nis)
                                preferences!!.setValues("nip", nip)
                                preferences!!.setValues("namaSekolah", namaSekolah)
                                preferences!!.setValues("kelas", kelas)
                                preferences!!.setValues("foto", foto)
                                Log.d("LOGIN Firebase ", "Data success " + preferences!!.getValues("nama") + " " + kodesekolah)
                                sessionManager!!.createSession(editTextEmail!!.text.toString())
                                Toast.makeText(applicationContext, "Berhasil login!", Toast.LENGTH_SHORT).show()
                                when (level) {
                                    "DOKTER" -> {
                                        val j = Intent(this@Login, TabbedDoktor::class.java)
                                        //                                            Intent j = new Intent(Login.this, NewLayoutActivity.class);
                                        startActivity(j)
                                        finish()
                                    }
                                    "KS" -> {
                                        val j = Intent(this@Login, TabbedKepsek::class.java)
                                        startActivity(j)
                                        finish()
                                    }
                                    "GURU", "WALIKELAS" -> {
                                        val j = Intent(this@Login, TabbedGuru::class.java)
                                        startActivity(j)
                                        finish()
                                    }

                                    "SISWA" -> {
                                        val intent = Intent(this@Login, Tabbed::class.java)
                                        intent.putExtra("level", level)
                                        startActivity(intent)
                                        finish()
                                    }

                                    else -> {
                                        val j = Intent(this@Login, Tabbed::class.java)
                                        startActivity(j)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                loading!!.dismiss()
                                Log.d("cancelled ", " true ")
                            }
                        })
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
                        Log.d("LOGIN Firebase ", " failed")
                        Toast.makeText(applicationContext, "Gagal login!", Toast.LENGTH_SHORT).show()
                        loading!!.dismiss()
                        //                            Toast.makeText(Login.this, "Akun belum terdaftar, Silahkan daftar terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }
                }
    }
}
package com.projectbelajar.yuukbelajar

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.projectbelajar.yuukbelajar.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.infoguru.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Login : AppCompatActivity(){

    // global var
    private var id : String ?= null
    private var name : String ?= null
    private var level : String ?= null
    private var kodesekolah : String ?= null
    private var nis : String ?= null
    private var nip : String ?= null
    private var foto : String ?= null
    private var password : String ?= null
    private var nohp : String ?= null
    private var email : String ?= null
    private var namaSekolah : String ?= null
    private var kelas : String ?= null
    private var jurusan : String ?= null

    var binding : ActivityLoginBinding?= null

    //Defining views
    var doubleBackToExitPressedOnce = false
    private var context: Context? = null
    private var JSON_STRING: String? = null
    var sessionManager: SessionManager? = null

    //firebase
    private var firebaseUser: FirebaseUser? = null
    private var auth: FirebaseAuth? = null
    private var reference = FirebaseDatabase.getInstance().getReference("User")
    private var preferences: Preferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        attachBtn()
        validationEmail()


        //firebase
        preferences = Preferences(this)
        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        sessionManager = SessionManager(applicationContext)
        context = this@Login

    }

    private fun validationEmail() {
        binding?.etEmail?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding?.etEmail?.text.toString()).matches()){
                    binding?.btnLogin?.isEnabled = true
                }
                else{
                    binding?.btnLogin?.isEnabled = false
                    binding?.etEmail?.error = "Email Tidak Valid"
                }
            }
        })
    }


    fun attachBtn() {

         binding?.btnLogin?.setOnClickListener {
             email = binding?.etEmail?.text.toString().trim{ it <= ' '}
             password = binding?.etPassword!!.text.toString().trim { it <= ' ' }

             if (email.isNullOrEmpty()){
                 binding?.etEmail?.error = "Email Tidak Boleh kosong"
                 binding?.etEmail?.requestFocus()

             }else if (password.isNullOrEmpty()){
                 binding?.etPassword?.error = "Email Tidak Boleh kosong"
                 binding?.etPassword?.requestFocus()
             }else{
                 login()
             }
         }

         binding?.btnForgot?.setOnClickListener {
             startActivity(Intent(this@Login, Lupa::class.java))
         }
        binding?.btnRegister?.setOnClickListener {
            startActivity(Intent(this@Login, Registrasi::class.java))
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
                    jsonObject = JSONObject(JSON_STRING ?: "")
                    val result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY)
                    for (i in 0 until result.length()) {
                        val jo = result.getJSONObject(i)
                        val ket1 = jo.getString(konfigurasi.TAG_KET)
                        level = jo.getString(konfigurasi.TAG_POSISI)
                        jurusan = jo.getString(konfigurasi.TAG_JURUSAN)
                        name = jo.getString("name")
                        nis = jo.getString("noinduk")
                        namaSekolah = jo.getString("nmskl")
                        email = jo.getString("email")
                        kodesekolah = jo.getString("kdskl")
                        kelas = jo.getString("kls")
                        password = binding?.etPassword!!.text.toString().trim { it <= ' ' }



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
                            Log.d("login ", " as $result")
                            loginFireBase(email, password, level!!, loading)

                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "SUKSES" && (level == "ORANG TUA" || level == "WALI MURID")) {
                            Log.d("login ", " as $level")
                            loginFireBase(email, password, level!!, loading)
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "SUKSES" && level == "WALIKELAS" || level == "GURU") {
                            Log.d("login ", " as $level")
                            loginFireBase(email, password, level!!, loading)
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        }
                        else if (ket1 == "SUKSES" && level == "DOKTER") {
                            Log.d("login ", " as $level")
                            loginFireBase(email, password, level!!, loading)
                            //                            sessionManager.createSession(editTextEmail.getText().toString());
//                            Toast.makeText(getApplicationContext(), "LOGIN " + ket1, Toast.LENGTH_SHORT).show();
//                            Intent j = new Intent(Login.this, Tabbed.class);
//                            j.putExtra("level", level);
//                            startActivity(j);
                        } else if (ket1 == "GAGAL") {

                            loading?.dismiss()
                            Toast.makeText(this@Login, "Email atau Password salah", Toast.LENGTH_SHORT).show()

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
                    loading?.dismiss()
                    Toast.makeText(this@Login, "Something Wrong ...", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun doInBackground(vararg params: Void?): String? {
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
    private fun loginFireBase(email1: String?, password1: String?, level: String, loading: ProgressDialog?= null) {
        auth!!.signInWithEmailAndPassword(email1 ?: "", password1 ?: "")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        readData(reference, object : OnGetDataListener {
                            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                                for (data in dataSnapshot?.children!!){
                                    if (auth?.currentUser?.email == data.child("email").value.toString()){
                                        loading!!.dismiss()
                                        id = firebaseUser?.uid
                                        name = data.child("nama").value.toString()
                                        kodesekolah = data.child("kodeSekolah").value.toString()
                                        nis = data.child("nis").value.toString()
                                        nip = data.child("nip").value.toString()
                                        foto = data.child("imgUrl").value.toString()
                                        password = data.child("password").value.toString()
                                        nohp = data.child("username").value.toString()
                                        email = data.child("email").value.toString()
//                                        namaSekolah = data.child("namaSekolah").value.toString()
                                        kelas = data.child("kelas").value.toString()


                                        when(kodesekolah){
                                            "5114" -> namaSekolah = "SMAN 4 BANJARMASIN"
                                            "5115" -> namaSekolah = "SMAN 5 BANJARMASIN"
                                            "5116" -> namaSekolah = "SMAN 6 BANJARMASIN"
                                            "5118" -> namaSekolah = "SMAN 8 BANJARMASIN"
                                            "51110" -> namaSekolah = "SMAN 10 BANJARMASIN"
                                        }

                                        setPreference()
                                        updateUi()
                                    }
                                }


                            }

                            override fun onStart() {
                                //when starting
                                Log.d("ONSTART", "Started")
                            }

                            override fun onFailure() {
                                Log.d("onFailure", "Failed")
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

                        registerFirebase("", email ?: "", password ?: "", level,
                                kodesekolah ?: "", nis ?: "", name ?: "",
                                    namaSekolah ?: "", kelas ?: ""
                                )

//                            circularProgressBar.setVisibility(View.GONE);
//                            bgLoad.setVisibility(View.GONE);
//                        Log.d("LOGIN Firebase ", " failed")
//                        Toast.makeText(applicationContext, "Gagal login!", Toast.LENGTH_SHORT).show()
//                        loading!!.dismiss()
                        //                            Toast.makeText(Login.this, "Akun belum terdaftar, Silahkan daftar terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    private fun setPreference() {
        preferences?.setValues("id", id)
        preferences?.setValues("hp", nohp)
        preferences?.setValues("password", password)
        preferences?.setValues("email", email)
        preferences?.setValues("nama", name)
        preferences?.setValues("kodesekolah", kodesekolah)
        preferences?.setValues("nis", nis)
        preferences?.setValues("nip", nip)
        preferences?.setValues("namaSekolah", namaSekolah)
        preferences?.setValues("kelas", kelas)
        preferences?.setValues("foto", foto)
        Log.d("LOGIN Firebase ", "Data success " + preferences!!.getValues("nama") + " " + kodesekolah)
        sessionManager?.createSession(binding?.etEmail?.text.toString())
        preferences?.setValues("level", level)
        preferences?.setValues("jurusan", jurusan)
    }

    private fun updateUi() {

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

    fun readData(ref: DatabaseReference, listener: OnGetDataListener) {
        listener.onStart()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener.onSuccess(dataSnapshot)
            }
        })
    }

    interface OnGetDataListener {
        //this is for callbacks
        fun onSuccess(dataSnapshot: DataSnapshot?)
        fun onStart()
        fun onFailure()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun registerFirebase(username: String, email: String, password: String, level: String, kodeSekolah: String, nis: String, nama: String, namaSekolah: String, kelas: String) {
        auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth!!.currentUser
                        id = firebaseUser!!.uid
                        reference = FirebaseDatabase.getInstance().getReference("User").child(id ?: "")
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = id ?: ""
                        hashMap["username"] = username
                        hashMap["level"] = level
                        hashMap["kodeSekolah"] = kodeSekolah
                        hashMap["nis"] = nis
                        hashMap["nama"] = nama
                        hashMap["sort_nama"] = nama.toLowerCase()
                        hashMap["email"] = email
                        hashMap["long"] = 0.1
                        hashMap["lat"] = 0.1
                        hashMap["namaSekolah"] = namaSekolah
                        hashMap["kelas"] = kelas
                        hashMap["status"] = "offline"
                        hashMap["imgUrl"] = "default"
                        reference!!.setValue(hashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                setPreference()
                                updateUi()
                            } else {
                                Toast.makeText(this@Login, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@Login, "Registrasi gagal!", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
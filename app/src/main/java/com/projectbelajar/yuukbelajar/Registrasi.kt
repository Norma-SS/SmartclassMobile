package com.projectbelajar.yuukbelajar

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.projectbelajar.yuukbelajar.Registrasi
import org.json.JSONException
import org.json.JSONObject
import java.util.*

//import android.support.v7.app.AppCompatActivity;
class Registrasi : AppCompatActivity(), View.OnClickListener {
    //Defining views
    private var editTextKdSkl: EditText? = null
    private var editTextId: EditText? = null
    private var editTextNama: EditText? = null
    private val stts: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextHp: EditText? = null
    private var editTextPassword: EditText? = null

    //private Context context;
    private var buttonRegister: Button? = null
    private var ShowPass: CheckBox? = null
    private var spinner: Spinner? = null
    private var JSON_STRING: String? = null

    //firebase
    private var firebaseUser: FirebaseUser? = null
    private var auth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null
    private var preferences: Preferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)
        preferences = Preferences(this)

        //firebase
        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        spinner = findViewById<View>(R.id.stts) as Spinner
        spinner!!.onItemSelectedListener = ItemSelectedListener()


        //Inisialisasi dari View
        editTextKdSkl = findViewById<View>(R.id.editTextKdSkl) as EditText
        editTextId = findViewById<View>(R.id.editTextId) as EditText
        editTextNama = findViewById<View>(R.id.editTextnama) as EditText
        editTextHp = findViewById<View>(R.id.editTextHp) as EditText
        editTextEmail = findViewById<View>(R.id.editTextEmail) as EditText
        editTextPassword = findViewById<View>(R.id.editTextPassword) as EditText

        buttonRegister = findViewById<View>(R.id.buttonRegister) as Button
        buttonRegister!!.setOnClickListener(this)
    }

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private fun addEmployee() {
        val kdskl = editTextKdSkl!!.text.toString().trim { it <= ' ' }
        val id = editTextId!!.text.toString().trim { it <= ' ' }
        val nama = editTextNama!!.text.toString().trim { it <= ' ' }
        val stts2 = spinner!!.selectedItem.toString().toUpperCase()
        val hp = editTextHp!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }

        //Toast.makeText(getApplicationContext(), stts1, Toast.LENGTH_LONG).show();
        class AddEmployee : AsyncTask<Void?, Void?, String?>() {
            var loading: ProgressDialog? = null
            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(this@Registrasi, "Menambahkan...", "Tunggu...", false, false)
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                loading!!.dismiss()
                JSON_STRING = s
                showEmployee()
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
//                Intent i = new Intent(Registrasi.this, Login.class);
//                startActivity(i);
                Log.d("status", stts2)
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
                        val ket2 = jo.getString(konfigurasi.TAG_KETER2)
                        val namaSekolah = jo.getString("nmskl")
                        val kelas = jo.getString("kelas")

//                        Toast.makeText(getApplicationContext(), ket1, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), ket2, Toast.LENGTH_LONG).show();
                        Log.d("ket 1 ", ket1)
                        Log.d("ket 2 ", ket2)
                        if (ket2 == "Terima Kasih") {
                            registerFirebase(hp, email, password, stts2, kdskl, id, nama, namaSekolah, kelas)
                            Toast.makeText(applicationContext, "Registrasi Berhasil", Toast.LENGTH_LONG).show()
                        } else if (namaSekolah.isNotEmpty()) {
                            Toast.makeText(applicationContext, "Anda Sudah Terdaftar di $namaSekolah pada kelas $kelas", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, ket1, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun doInBackground(vararg params: Void?): String? {
                val params = HashMap<String, String>()
                params[konfigurasi.KEY_EMP_KDSKL] = kdskl
                params[konfigurasi.KEY_EMP_ID] = id
                params[konfigurasi.KEY_EMP_NAMA] = nama
                params[konfigurasi.KEY_EMP_STTS] = stts2
                params[konfigurasi.KEY_EMP_HP] = hp
                params[konfigurasi.KEY_EMP_EMAIL] = email
                params[konfigurasi.KEY_EMP_PASSWORD_REGISTRASI] = password
                val rh = RequestHandler()
                return rh.sendPostRequest(konfigurasi.URL_ADD, params)
            }
        }

        val ae = AddEmployee()
        ae.execute()
    }

    override fun onClick(v: View) {
        if (v === buttonRegister) {
            if (editTextNama!!.length() == 0) {
                Toast.makeText(applicationContext, "nama tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else if (editTextEmail!!.length() == 0 && editTextPassword!!.length() == 0) {
                Toast.makeText(applicationContext, "email dan password tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else if (spinner!!.selectedItem.toString().toUpperCase() == "PILIH STATUS") {
                Toast.makeText(applicationContext, "Status belum dipilih", Toast.LENGTH_LONG).show()
            }
             else {
                addEmployee()
            }
        }
    }

    inner class ItemSelectedListener : OnItemSelectedListener {
        //get strings of first item
        var stts = spinner!!.selectedItem.toString()
        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
            val stts1 = spinner!!.selectedItem.toString()
            if (stts == spinner!!.selectedItem.toString()) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.context,
                        "Anda memilih : $stts1",
                        Toast.LENGTH_LONG).show()
                // Todo when item is selected by the user
            }
        }

        override fun onNothingSelected(arg: AdapterView<*>?) {}
    }

    private fun registerFirebase(username: String, email: String, password: String, level: String, kodeSekolah: String, nis: String, nama: String, namaSekolah: String, kelas: String) {
        auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        val firebaseUser = auth!!.currentUser
                        val userId = firebaseUser!!.uid
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = userId
                        hashMap["username"] = username
                        hashMap["level"] = level
                        hashMap["kodeSekolah"] = kodeSekolah
                        hashMap["nis"] = nis
                        hashMap["nama"] = nama
                        hashMap["sort_nama"] = nama.toLowerCase()
                        hashMap["email"] = email
                        hashMap["long"] = 0.1
                        hashMap["lat"] = 0.1
                        hashMap["password"] = password
                        hashMap["namaSekolah"] = namaSekolah
                        hashMap["kelas"] = kelas
                        hashMap["status"] = "offline"
                        hashMap["imgUrl"] = "default"
                        reference!!.setValue(hashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@Registrasi, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                preferences!!.setValues("nama", nama)
                                preferences!!.setValues("level", level)
                                preferences!!.setValues("kodesekolah", kodeSekolah)
                                preferences!!.setValues("nis", nis)
                                preferences!!.setValues("foto", "default")
                                //                                        circularProgressBar.setVisibility(View.GONE);
//                                        bgLoad.setVisibility(View.GONE);
                                val intent = Intent(this@Registrasi, Login::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@Registrasi, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@Registrasi, "Registrasi gagal!", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
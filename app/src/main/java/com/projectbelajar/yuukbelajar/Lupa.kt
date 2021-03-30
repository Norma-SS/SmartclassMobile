package com.projectbelajar.yuukbelajar

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.projectbelajar.yuukbelajar.databinding.ActivityLupaBinding
import java.util.*

class Lupa : AppCompatActivity(){

    private var binding : ActivityLupaBinding? = null
    private var editTextEmail: EditText? = null
    private var Pesan: TextView? = null
    private var Pesan2: TextView? = null
    private var buttonLupa: Button? = null
    private var JSON_STRING: String? = null
    private var reference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        attachBtn()
        validationEmail()

        reference = FirebaseDatabase.getInstance().reference.child("Users")
        Pesan = findViewById<View>(R.id.pesan) as TextView
        Pesan2 = findViewById<View>(R.id.pesan2) as TextView
    }

    private fun validationEmail() {
        binding?.editTextEmail?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding?.editTextEmail?.text.toString()).matches()){
                    binding?.btnLupaSend?.isEnabled = true
                }
                else{
                    binding?.btnLupaSend?.isEnabled = false
                    binding?.editTextEmail?.error = "Email Tidak Valid"
                }
            }
        })
    }

    fun attachBtn() {
         binding?.btnLupaSend?.setOnClickListener {
             if (binding?.editTextEmail?.text.isNullOrEmpty() || binding?.editTextEmail?.text?.trim() =="") {
                 editTextEmail?.error = "email tidak Boleh kosong"
             } else {
                 val auth = FirebaseAuth.getInstance()
                 val emailAddress = binding?.editTextEmail?.text.toString()
                 auth.sendPasswordResetEmail(emailAddress)
                         .addOnCompleteListener { task ->
                             if (task.isSuccessful) {
                                Toast.makeText(this@Lupa, "Email Reset Password Terkirim", Toast.LENGTH_LONG).show()
                             }
                         }
                 addEmployee()
             }
         }
    }

    private fun addEmployee() {
        val eml = binding?.editTextEmail?.text.toString().trim()

        //Toast.makeText(getApplicationContext(), epesan, Toast.LENGTH_LONG).show();
        class AddEmployee : AsyncTask<Void?, Void?, String?>() {
            var loading: ProgressDialog? = null
            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(this@Lupa, "checking email...", "wait...", false, false)
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                loading!!.dismiss()
                JSON_STRING = s
                showEmployee()
                //Toast.makeText(Registrasi.this,s,Toast.LENGTH_LONG).show();
                finish()
            }

            private fun showEmployee() {
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

             override fun doInBackground(vararg params: Void?): String? {
                val params = HashMap<String, String>()
                params[konfigurasi.KEY_EMP_EMAIL] = eml
                val rh = RequestHandler()
                return rh.sendPostRequest(konfigurasi.URL_LUPA, params)
            }
        }

        val ae = AddEmployee()
        ae.execute()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
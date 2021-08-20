package com.projectbelajar.yuukbelajar.ui.infoharian

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityHarianBinding
import com.projectbelajar.yuukbelajar.ui.infoharian.Harian
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Harian : AppCompatActivity(){

    private var binding : ActivityHarianBinding ?= null
    private var preferences : Preferences ?= null
    private var progressDialog : ProgressDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHarianBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initView()
        initNetwork()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding?.tvNama?.text = "Nama : " + preferences?.getValues("nama")
        binding?.tvKelas?.text = "Kelas : " + preferences?.getValues("kelas")

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun initNetwork() {
        NetworkConfig.service().getInfoQuiz(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.result.isNullOrEmpty()){
                        val toast = Toast.makeText(applicationContext, "Tidak Ada Data", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                    progressDialog?.dismiss()
                    binding?.rvInfoSikap?.adapter = PenilaianSikapAdapter(it?.result!!)
                },{
                    progressDialog?.dismiss()
                    Toast.makeText(applicationContext, "something wrong", Toast.LENGTH_SHORT).show()
                })
    }
}
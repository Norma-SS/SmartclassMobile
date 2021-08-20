package com.projectbelajar.yuukbelajar.ui.infouas

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityInfoUasBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class InfoUas : AppCompatActivity(){

    private var binding : ActivityInfoUasBinding ?= null
    private var preferences : Preferences ?= null
    private var progressDialog : ProgressDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUasBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initNetwork()
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding?.tvNama?.text = "Nama : " + preferences?.getValues("nama")
        binding?.tvKelas?.text =  "Kelas : " + preferences?.getValues("kelas")
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    @SuppressLint("SetTextI18n")
    private fun initNetwork() {
        NetworkConfig.service().getInfoUas(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.result.isNullOrEmpty()){
                        val toast = Toast.makeText(applicationContext, "Tidak Ada Data", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                    progressDialog?.dismiss()
                    binding?.rvInfoUts?.adapter = InfoUasAdapter(it?.result!!.reversed())
                },{
                    progressDialog?.dismiss()
                    Toast.makeText(applicationContext, "something wrong", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        preferences = null
    }
}
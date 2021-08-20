package com.projectbelajar.yuukbelajar.ui.infouts

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityInfoUtsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class InfoUts : AppCompatActivity(){

    private var binding : ActivityInfoUtsBinding ?= null
    private var preferences : Preferences?= null
    private var progressDialog : ProgressDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUtsBinding.inflate(layoutInflater)
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


    private fun initNetwork(){
        NetworkConfig.service().getInfoUts(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.result.isNullOrEmpty()){
                        val toast = Toast.makeText(applicationContext, "Tidak Ada Data", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                    progressDialog?.dismiss()
                    binding?.rvInfoUts?.adapter = InfoUtsAdapter(it.result!!.reversed())
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
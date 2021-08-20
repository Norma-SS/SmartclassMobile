package com.projectbelajar.yuukbelajar.ui.infosekolah

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityInfoSekolahBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class InfoSekolah : AppCompatActivity(){

    private var binding : ActivityInfoSekolahBinding ?= null
    private var preference : Preferences ?= null
    private var progressDialog : ProgressDialog?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoSekolahBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preference = Preferences(this)

        initView()
        initNetwork()
        initBtn()
    }

    private fun initView() {
        binding?.tvNama?.text = "Nama : " + preference?.getValues("nama")
        binding?.tvKelas?.text = "Kelas : " + preference?.getValues("kelas")
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun initNetwork() {
        NetworkConfig.service().getInfoSekolah(preference?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.result.isNullOrEmpty()){
                          val toast = Toast.makeText(applicationContext, "Tidak Ada Data", Toast.LENGTH_LONG)
                                  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                                  toast.show()
                    }
                    progressDialog?.dismiss()
                    binding?.rvAnnouncement?.adapter = InfoSekolahAdapter(it.result!!.reversed())
                },{
                    progressDialog?.dismiss()
                    Toast.makeText(applicationContext, "something wrong", Toast.LENGTH_SHORT).show()                })
    }

    private fun initBtn() {
        binding?.tvFilter?.setOnClickListener {
            Toast.makeText(applicationContext, "Fitur Ini belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
        preference = null
    }
}
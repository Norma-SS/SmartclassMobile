package com.projectbelajar.yuukbelajar.ui.infouts

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityInfoUtsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class InfoUts : AppCompatActivity(){

    private var binding : ActivityInfoUtsBinding ?= null
    private var preferences : Preferences?= null

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
    }


    private fun initNetwork(){
        NetworkConfig.service().getInfoUts(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding?.rvInfoUts?.adapter = InfoUtsAdapter(it.result!!.reversed())
                },{

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        preferences = null
    }
}
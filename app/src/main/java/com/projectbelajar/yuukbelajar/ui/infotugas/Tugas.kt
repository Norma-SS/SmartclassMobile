package com.projectbelajar.yuukbelajar.ui.infotugas

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityTugasBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class Tugas : AppCompatActivity(){

    private var binding : ActivityTugasBinding ?= null
    private var preferences : Preferences ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTugasBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initNetwork()
        initView()

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding?.tvNama?.text = "Nama : " + preferences?.getValues("nama")
        binding?.tvKelas?.text = "Kelas : " + preferences?.getValues("kelas")
    }

    private fun initNetwork() {
        NetworkConfig.service().getInfoTugas(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding?.rvTugas?.adapter = TugasAdapter(it?.result!!.reversed())
                },{

                })
    }
}
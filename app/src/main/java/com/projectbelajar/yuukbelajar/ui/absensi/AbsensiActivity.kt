package com.projectbelajar.yuukbelajar.ui.absensi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityAbsensiBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG = "AbsensiActivity"
class AbsensiActivity : AppCompatActivity() {

    private var binding : ActivityAbsensiBinding ?= null
    private var prefereces : Preferences ?= null

    private var email : String ?= null
    private var ket : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsensiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initVar()
        attachBtn()
    }

    private fun initVar() {
        prefereces = Preferences(this)
        email = prefereces?.getValues("email")
    }

    private fun attachBtn() {
            binding?.btnKirimAbsen?.setOnClickListener {
                val ket = binding?.spinner?.selectedItem?.toString()
                if (ket == "Pilih Keterangan"){
                    Toast.makeText(applicationContext, "Silahkan Pilih Keterangan Absensi", Toast.LENGTH_SHORT).show()
                }else{
                    NetworkConfig.service().insertAbsenSiswa(email ?: "", ket ?: "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                if (it.result?.get(0)?.isSuccess == true){
                                    Toast.makeText(applicationContext, "Absensi berhasil", Toast.LENGTH_SHORT).show()
                                    finish()
                                }else{
                                    Toast.makeText(applicationContext, it.result?.get(0)?.msg.toString(), Toast.LENGTH_SHORT).show()
                                }
                            },{

                            })
                }
        }
    }
}
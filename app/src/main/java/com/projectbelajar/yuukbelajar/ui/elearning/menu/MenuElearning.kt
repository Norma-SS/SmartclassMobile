package com.projectbelajar.yuukbelajar.ui.elearning.menu

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityMenuElearningBinding
import com.projectbelajar.yuukbelajar.ui.elearning.Elearning
import com.projectbelajar.yuukbelajar.ui.elearning.menu.MenuElearning
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

class MenuElearning : AppCompatActivity(){

    private var binding : ActivityMenuElearningBinding ?= null
    private var preference : Preferences ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuElearningBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preference = Preferences(this)

        initView()
        initNetwork()

    }

    private fun initView() {
        binding?.tvNamaSekolah?.text = preference?.getValues("namaSekolah")
        binding?.tvKelas?.text = preference?.getValues("kelas")
    }

    private fun initNetwork() {
        NetworkConfig.service().getdataMapel(preference?.getValues("kodesekolah") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding?.rvMenuElearning?.adapter = MenuElearningAdapter(this@MenuElearning, it.result?.datanya!!)
                },{

                })

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        preference = null
    }
}
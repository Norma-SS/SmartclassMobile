package com.projectbelajar.yuukbelajar.ui.elearning

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityElearningBinding
import com.projectbelajar.yuukbelajar.ui.elearning.Elearning
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Elearning : AppCompatActivity() {

    private var materi : String ?= null
    private var emlx : String ?= null
    private var preferences : Preferences ?= null
    private var binding : ActivityElearningBinding ?= null
    private var jurusan : String ?= null
    private var kodeSekolah : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElearningBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initvar()
        initNetwork()

    }

    private fun initvar() {
        materi = intent.getStringExtra("materi")
        jurusan = preferences?.getValues("kelas")
        kodeSekolah = preferences?.getValues("kodesekolah")
    }

    @SuppressLint("SetTextI18n")
    private fun initNetwork() {
        NetworkConfig.service().getBelon(jurusan ?: "", materi ?: "", kodeSekolah ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding?.tvSekolah?.text = it.resultBelon?.get(0)?.nmskl
                    binding?.tvMapel?.text = "MAPEL : " + it.resultBelon?.get(0)?.nmmapel
                    binding?.rvListBelajar?.adapter = ElearningAdapter(this@Elearning, it?.resultBelon!!.reversed(), this.lifecycle)
                },{

                })
    }

}
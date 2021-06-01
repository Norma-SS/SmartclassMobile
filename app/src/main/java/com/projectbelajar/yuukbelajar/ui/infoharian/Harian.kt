package com.projectbelajar.yuukbelajar.ui.infoharian

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityHarianBinding
import com.projectbelajar.yuukbelajar.ui.infoharian.Harian
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Harian : AppCompatActivity(){

    private var binding : ActivityHarianBinding ?= null
    private var preferences : Preferences ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHarianBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initNetwork()
    }

    private fun initNetwork() {

    }
}
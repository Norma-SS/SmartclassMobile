package com.projectbelajar.yuukbelajar.ui.ujianonline

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityQuizOnlineBinding
import com.projectbelajar.yuukbelajar.ui.ujianonline.QuizOnline
import com.projectbelajar.yuukbelajar.ui.ujianonline.UjianOnline.CounterClass
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "QuizOnline"
class QuizOnline : AppCompatActivity() {

    private var binding : ActivityQuizOnlineBinding ?= null
    private var preferences : Preferences ?= null

    private var progressDialog : ProgressDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizOnlineBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initVar()
        initNetwork()
    }

    private fun initVar() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data Ujian...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun initNetwork() {
        NetworkConfig.service().getQuiz(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "Datanya : $it")
                    if (it.result?.isNotEmpty() == true){
                        if (it.result?.get(0)?.isSuccess == false){
                            setView("empty")
                            progressDialog?.dismiss()
                        }else{
                            setView("quiz")
                            binding?.NmSkl?.text = it.result?.get(0)?.nmskl
                            binding?.rvListQuiz?.adapter = QuizOnlineAdapter(this@QuizOnline, it?.result!!)
                            progressDialog?.dismiss()
                        }
                    }else{
                            progressDialog?.dismiss()
                            setView("empty")
                    }


                },{

                })
    }

    private fun setView(type : String) {
        when(type){
            "empty" ->  {
                binding?.ivNodata?.visibility = View.VISIBLE
                binding?.tvNodata?.visibility = View.VISIBLE
            }
            "quiz" -> {
                binding?.ivNodata?.visibility = View.GONE
                binding?.tvNodata?.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
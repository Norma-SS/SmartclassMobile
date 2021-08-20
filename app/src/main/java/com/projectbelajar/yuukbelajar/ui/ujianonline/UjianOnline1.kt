package com.projectbelajar.yuukbelajar.ui.ujianonline

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.data.network.model.request.RequestDoneExam
import com.projectbelajar.yuukbelajar.data.network.model.response.AcakSoalItem
import com.projectbelajar.yuukbelajar.databinding.ActivityUjianOnline1Binding
import com.projectbelajar.yuukbelajar.utils.FullScreenHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_one_button.view.*
import kotlinx.android.synthetic.main.dialog_review_ujian.view.*
import kotlinx.android.synthetic.main.dialog_two_button.view.*
import kotlinx.android.synthetic.main.dialog_two_button.view.btn_selesai
import kotlinx.android.synthetic.main.dialog_youtube.view.*
import java.util.concurrent.TimeUnit

private const val TAG = "UjianOnline1"
class UjianOnline1 : AppCompatActivity() {

    private var binding : ActivityUjianOnline1Binding ?= null
    private var preference : Preferences ?= null
    private var fullScreenHelper : FullScreenHelper?= null

    private var soal_id : String ?= null
    private var kodeSekolah : String ?= null
    private var email : String ?= null
    private var noSoal : String ?= null

    private var index : Int = 0

    private var progressDialog : ProgressDialog ?= null
    private lateinit var dialogView : Dialog
    private var view_dialog : View?= null

    private lateinit var hashMap : HashMap<Int, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUjianOnline1Binding.inflate(layoutInflater)
        setContentView(binding?.root)
        preference = Preferences(this)

        initVar()
        initNetwork()
    }


    private fun initVar(){
        fullScreenHelper = FullScreenHelper(this)

        soal_id = intent.getStringExtra("soal_id")
        email = preference?.getValues("email")
        kodeSekolah = preference?.getValues("kodesekolah")

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data Ujian...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun initNetwork() {
        NetworkConfig.service().acakSoal(email ?: "", soal_id ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setData(it.data)
                    timer(it.data?.get(0)?.durasi?.times(60000)?.toLong())
                    initView(it.data)
                    progressDialog?.dismiss()
                },{
                    progressDialog?.dismiss()
                })
    }

    @SuppressLint("SetTextI18n")
    private fun initView(data: List<AcakSoalItem?>?) {
        binding?.tvJudul?.text = data?.get(0)?.jdluji
        binding?.tvNamaSekolah?.text = data?.get(0)?.nmskl
        binding?.tvNamaMapel?.text = data?.get(0)?.mapel
        binding?.tvJmlSoal?.text = data?.get(0)?.jumlahSoal.toString() + " soal "
        binding?.tvNamaNis?.text = data?.get(0)?.name + " - " + data?.get(0)?.id

    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: List<AcakSoalItem?>?) {

        val totalIndex = data?.size
        hashMap = HashMap<Int, Any>()

        bindUi(index, data)

        binding?.btnNext?.setOnClickListener {

            checkRadio(index)
            binding?.radioGroup?.clearCheck()

            if (index < totalIndex?.minus(1) ?: 0) {
                index += 1

                bindRadioSaved(hashMap, index)

                bindUi(index, data)
            } else {
                Toast.makeText(applicationContext, "Ini Soal yang Terakhir", Toast.LENGTH_SHORT).show()
                bindRadioSaved(hashMap, index)
            }
        }

        binding?.btnPrev?.setOnClickListener {
            checkRadio(index)
            binding?.radioGroup?.clearCheck()

            if (index > 0 ) {
                index -= 1

                bindRadioSaved(hashMap, index)

                bindUi(index, data)
            } else {
                Toast.makeText(applicationContext, "Ini Soal yang Pertama", Toast.LENGTH_SHORT).show()
                bindRadioSaved(hashMap, index)
            }

        }

        binding?.btnFinish?.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_two_button, null)
            dialog.setView(view)
            dialogView = dialog.create()
            dialogView = dialog.show()

            view.btn_selesai.setOnClickListener {
                checkRadio(index)

                NetworkConfig.service().hitDone(RequestDoneExam(soal_id, email, kodeSekolah))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "${it.isSuccess}")
                        },{
                            Log.d(TAG, it.localizedMessage)
                        })

                NetworkConfig.service().getScore(kodeSekolah ?: "", soal_id ?: "", email ?: "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            showDialogEnd(it.score ?: "")
                        },{})
            }

            view.btn_batal.setOnClickListener{
                dialogView.dismiss()
            }
        }

        binding?.btnFabReview?.setOnClickListener {
            checkRadio(index)
            showDialogReview(totalIndex, hashMap, data)
        }

        binding?.btnFabVideo?.setOnClickListener {
            showDialogVideo(data)
        }

    }

    override fun onBackPressed() {}



    private fun timer(durasi: Long?) {
        val timer = object: CountDownTimer(durasi!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = String.format(
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(millisUntilFinished)))
                binding?.tvDurasi?.text = "$hms"
            }
            override fun onFinish() {
                Toast.makeText(applicationContext, "Waktu Ujian Sudah Habis", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        timer.start()
    }

    private fun checkRadio(index : Int){
        when {
            binding?.radio1?.isChecked == true ->{
                hashMap[index] = "A"
                postAnswer(soal_id ?: "", email ?: "", index.plus(1).toString(), noSoal ?: "", "1")
            }
            binding?.radio2?.isChecked == true ->{
                hashMap[index] = "B"
                postAnswer(soal_id ?: "", email ?: "", index.plus(1).toString(), noSoal ?: "", "2")
            }
            binding?.radio3?.isChecked == true ->{
                hashMap[index] = "C"
                postAnswer(soal_id ?: "", email ?: "", index.plus(1).toString(), noSoal ?: "", "3")
            }
            binding?.radio4?.isChecked == true -> {
                hashMap[index] = "D"
                postAnswer(soal_id ?: "", email ?: "", index.plus(1).toString(), noSoal ?: "", "4")
            }
            binding?.radio5?.isChecked == true -> {
                hashMap[index] = "E"
                postAnswer(soal_id ?: "", email ?: "", index.plus(1).toString(), noSoal ?: "", "5")
            }
        }
    }

    private fun postAnswer(kdsoal : String, eml : String, noUrut : String, noSoal : String, answer : String){
        NetworkConfig.service().saveJawaban(kdsoal, eml, noUrut, noSoal, answer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                },{

                })
    }

    private fun bindRadioSaved(hashMap: HashMap<Int, Any>, index: Int) {
        when(hashMap[index]){
            "A" -> binding?.radio1?.isChecked = true
            "B" -> binding?.radio2?.isChecked = true
            "C" -> binding?.radio3?.isChecked = true
            "D" -> binding?.radio4?.isChecked = true
            "E" -> binding?.radio5?.isChecked = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindUi(index: Int, data: List<AcakSoalItem?>?) {
        noSoal = data!![index]?.nosoal
        binding?.tvNoSoal?.text = index.plus(1).toString() + "."
        binding?.tvSoal?.text = data[index]?.soal

        binding?.radio1?.text = "A." + data[index]?.A
        binding?.radio2?.text = "B." + data[index]?.B
        binding?.radio3?.text = "C." + data[index]?.C
        binding?.radio4?.text = "D." + data[index]?.D
        binding?.radio5?.text = "E." + data[index]?.E
    }



    private fun showDialogEnd(nilai : String){
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_one_button, null)
        dialog.setView(view)
        dialogView = dialog.create()
        dialogView = dialog.show()
        dialogView.setCancelable(false)

        view.tv_Nilai.text = nilai
        view.btn_selesai.setOnClickListener {
            finish()
        }
    }

    private fun showDialogReview(totalIndex: Int?, hashMap: HashMap<Int, Any>, data: List<AcakSoalItem?>?) {
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_review_ujian, null)
        view.rv_review_ujian.adapter = ReviewUjianAdapter(totalIndex ?: 0, hashMap, object : ReviewUjianAdapter.OnItemClick{
            override fun klikItem(position: Int) {
                binding?.radioGroup?.clearCheck()
                index = position
                bindUi(position, data)
                bindRadioSaved(hashMap, position)
                dialogView.dismiss()
            }
        })
        dialog.setView(view)
        dialogView = dialog.create()
        dialogView = dialog.show()

        view.btn_close.setOnClickListener {
            dialogView.dismiss()
        }

    }

    private fun showDialogVideo(data: List<AcakSoalItem?>?) {
        val dialog = AlertDialog.Builder(this)

        view_dialog = layoutInflater.inflate(R.layout.dialog_youtube, null)

        dialog.setView(view_dialog)
        dialogView = dialog.create()
        dialogView = dialog.show()
        dialogView.setCancelable(false)

        val link = data?.get(0)?.link
        val videoId: String? = link?.substringAfterLast("embed/")

        view_dialog?.btn_close_youtube?.setOnClickListener {
            dialogView.dismiss()
        }

        view_dialog?.tv_ket?.text = data?.get(index)?.ketSoal

        view_dialog?.youtube_player_dialog?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)

                youTubePlayer.loadOrCueVideo(lifecycle, videoId ?: "", 0f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        view_dialog?.youtube_player_dialog?.release()
        binding = null
    }
}
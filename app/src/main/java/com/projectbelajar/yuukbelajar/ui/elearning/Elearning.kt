package com.projectbelajar.yuukbelajar.ui.elearning

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.data.network.model.request.elearning.RequestElearning
import com.projectbelajar.yuukbelajar.data.network.model.response.ResultBelon
import com.projectbelajar.yuukbelajar.databinding.ActivityElearningBinding
import com.projectbelajar.yuukbelajar.utils.FullScreenHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat

const val TAG = "ElearningActivity"
class Elearning : AppCompatActivity() {

    private var nis : String ?= null
    private var nama : String ?= null
    private var kelas : String ?= null
    private var idBelon : String ?= null
    private var jurusan : String ?= null
    private var kdskl : String ?= null
    private var materi : String ?= null

    private var preferences : Preferences ?= null
    private var binding : ActivityElearningBinding ?= null


    private var linkDownload : String ?= null
    private var namaFile : String ?= null

    private val STORAGE_PERMISSION_CODE : Int = 1000

    private var fullScreenHelper : FullScreenHelper?= null
    private var videoID : String = "12"
    private var globalYouTubePlayer : YouTubePlayer ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElearningBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this)

        initvar()
        initNetwork()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding?.youtubePlayerView?.getPlayerUiController()?.getMenu()?.dismiss()

    }

    override fun onBackPressed() {
        if (binding?.youtubePlayerView?.isFullScreen()!!)
            binding?.youtubePlayerView?.exitFullScreen()

        else super.onBackPressed()
    }


    private fun addFullScreenListenerToPlayer() {
        binding?.youtubePlayerView?.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                fullScreenHelper!!.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                fullScreenHelper!!.exitFullScreen()
            }
        })
    }

    private fun initvar() {
        fullScreenHelper = FullScreenHelper(this)

        materi = intent.getStringExtra("materi")
        nis = preferences?.getValues("nis")
        nama = preferences?.getValues("nama")
        kelas = preferences?.getValues("kelas")
        jurusan = preferences?.getValues("jurusan")
        kdskl = preferences?.getValues("kodesekolah")

        Log.d(TAG, "kodeSekolah : $kdskl")
    }

    @SuppressLint("SetTextI18n")
    private fun initNetwork() {
        NetworkConfig.service().getBelon(kelas ?: "", materi ?: "", kdskl ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.resultBelon?.isNullOrEmpty() == true){
                        setView("empty")
                    }

                    val resultBelon = it.resultBelon.get(0)
                    binding?.tvSekolah?.text = resultBelon.nmskl
                    binding?.tvMapel?.text = "MAPEL : " + resultBelon.nmmapel

                    binding?.youtubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadOrCueVideo(lifecycle, resultBelon?.link ?: "", 0f)
                            addFullScreenListenerToPlayer()

                            idBelon = resultBelon.id
                            binding?.tvNamaGuru?.text = resultBelon?.nmguru
                            binding?.tvWaktuUpload?.text = resultBelon?.tgl?.formatDate()
                            binding?.tvJudul?.text = resultBelon?.judul
                            globalYouTubePlayer = youTubePlayer
                            postAbsent()
                        }
                    })

                    binding?.rvListBelajar?.adapter = ElearningAdapter(it?.resultBelon, object : ElearningAdapter.OnClick{
                        override fun clickItem(item: ResultBelon) {
                            idBelon = item?.id

                            binding?.tvNamaGuru?.text = item.nmguru
                            binding?.tvWaktuUpload?.text = item.tgl?.formatDate()
                            binding?.tvJudul?.text = item.judul
                            globalYouTubePlayer?.loadOrCueVideo(lifecycle, item.link ?: "", 0f)
                            addFullScreenListenerToPlayer()
                            postAbsent()
                        }

                        override fun downloadFile(item: ResultBelon) {
                            linkDownload = item?.path
                            namaFile = item?.namaFile
                            idBelon = item?.id

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                        PackageManager.PERMISSION_DENIED){
                                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                                }else{
                                    startDownloading()
                                }
                            }else{
                                startDownloading()
                            }

                        }
                    })
                },{

                })
    }

    private fun startDownloading() {
        val request = DownloadManager.Request(Uri.parse(linkDownload))

        Toast.makeText(applicationContext, "Sedang Men-Download", Toast.LENGTH_LONG).show()

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(namaFile)

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, namaFile)

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        postAbsent()
    }

    private fun postAbsent() {
        NetworkConfig.service().insert_absent_elearning(RequestElearning(
                nis ?: "",
                kdskl ?: "",
                nama ?: "",
                kelas ?: "",
                materi ?: "",
                idBelon ?: ""
        ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(applicationContext, "berhasil absen", Toast.LENGTH_SHORT).show()
                },{
                    Log.d(TAG, "${it.localizedMessage}")
                })
    }

    private fun setView(type: String) {
        when(type){
            "empty" -> {
                binding?.youtubePlayerView?.visibility = View.GONE
                binding?.cardView?.visibility = View.GONE
                binding?.rvListBelajar?.visibility = View.GONE
                binding?.tvSekolah?.visibility = View.GONE
                binding?.tvMapel?.visibility = View.GONE
                binding?.ivVideo?.visibility = View.VISIBLE
                binding?.tvEmptyVideo?.visibility = View.VISIBLE
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownloading()
                }else{

                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun String.formatDate(output : String = "dd MMM yyyy", input : String = "yyyy-MM-dd") : String{
        val inSdf = SimpleDateFormat(input)
        val outSdf = SimpleDateFormat(output)
        val date = inSdf.parse(this)
        return outSdf.format(date)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding?.youtubePlayerView?.release()
        binding = null
    }
}
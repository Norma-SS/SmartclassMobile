package com.projectbelajar.yuukbelajar.ui.infotugas

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.data.network.model.response.AcakSoalItem
import com.projectbelajar.yuukbelajar.data.network.model.response.TugasItem
import com.projectbelajar.yuukbelajar.databinding.ActivityTugasBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_youtube.view.*

private const val TAG = "Tugas"
class Tugas : AppCompatActivity(){

    private var binding : ActivityTugasBinding ?= null
    private var preferences : Preferences ?= null
    private var progressDialog : ProgressDialog?= null
    private var linkDownload : String ?= null
    private var namaFile : String ?= null

    private val STORAGE_PERMISSION_CODE : Int = 1000
    private lateinit var dialogView : Dialog

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

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading...")
        progressDialog?.setMessage("Sedang Memuat Data...")
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun initNetwork() {
        NetworkConfig.service().getInfoTugas(preferences?.getValues("email") ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.result.isNullOrEmpty()){
                        val toast = Toast.makeText(applicationContext, "Tidak Ada Data", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                    Log.d(TAG, "List Tugasnya ${it.result?.size}")
                    progressDialog?.dismiss()
                    binding?.rvTugas?.adapter = TugasAdapter(it?.result!!.reversed(), object : TugasAdapter.OnClick{
                        override fun downloadFile(item: TugasItem) {
                            linkDownload = item?.link
                            namaFile = item?.file
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
                    progressDialog?.dismiss()
                    Toast.makeText(applicationContext, "something wrong", Toast.LENGTH_SHORT).show()
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
    private fun showDialogVideo(data: List<AcakSoalItem?>?) {
        val dialog = AlertDialog.Builder(this)

        val view_dialog = layoutInflater.inflate(R.layout.dialog_youtube, null)

        dialog.setView(view_dialog)
        dialogView = dialog.create()
        dialogView = dialog.show()
        dialogView.setCancelable(false)

        val link = data?.get(0)?.link
        val videoId: String? = link?.substringAfterLast("embed/")

        view_dialog?.btn_close_youtube?.setOnClickListener {
            dialogView.dismiss()
        }

        view_dialog?.youtube_player_dialog?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)

                youTubePlayer.loadOrCueVideo(lifecycle, videoId ?: "", 0f)
            }
        })
    }
}
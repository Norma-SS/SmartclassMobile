package com.projectbelajar.yuukbelajar.ui.infotugas

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
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
import com.projectbelajar.yuukbelajar.databinding.ActivityUploadTugasBinding
import com.projectbelajar.yuukbelajar.utils.Constant
import com.projectbelajar.yuukbelajar.utils.FileUtils
import com.projectbelajar.yuukbelajar.utils.GalleryHelper
import id.zelory.compressor.Compressor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_youtube.view.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


private const val TAG = "UploadTugasActivity"
class UploadTugasActivity : AppCompatActivity() {

    private var uriFile : Uri ?= null
    private var nameFile : String ?= null
    private var CODE_PICK_FILE = 100
    private var binding : ActivityUploadTugasBinding ?= null
    private var preferences : Preferences ?= null

    private var data : TugasItem ?= null
    private var idTugas : String ?= null
    private var descTugas : String ?= null
    private var nis : String ?= null

    private lateinit var dialogView : Dialog
    private var view_dialog : View?= null

    private var compressedImageFile : File ?= null

    private var progressDialog : ProgressDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadTugasBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initVar()
        initView()

        GlobalScope.launch{
            attachBtn()
        }
    }

    private fun initView() {
        binding?.tvNamaGuru?.text = data?.namaguru
        binding?.tvDescTugas?.text = data?.deskripsi

        if (data?.linkyt.isNullOrEmpty()){
            binding?.btnFabVideo?.visibility = GONE
        }
    }

    private fun initVar() {
        data = intent.getParcelableExtra("data")
        preferences = Preferences(this)
        idTugas = intent.getStringExtra("idTugas")
        nis = preferences?.getValues("nis")
    }

   private suspend fun attachBtn() {

       binding?.btnFabVideo?.setOnClickListener {
           showDialogVideo()
       }

        binding?.btnPilihFile?.setOnClickListener {
            if (GalleryHelper.permissionGallery(this, this, CODE_PICK_FILE)){
                GalleryHelper.openGallery(this)
            }
        }

        binding?.btnUploadFile?.setOnClickListener {
            val komentar : String ?= binding?.etKomentar?.text.toString()
            progressDialog = ProgressDialog(this)
            progressDialog?.setTitle("Loading...")
            progressDialog?.setMessage("Sedang Meng-Upload...")
            progressDialog?.show()
            progressDialog?.setCancelable(false)
            Log.d(TAG, "Uri : $uriFile")
            Log.d(TAG, "etKomentar : $komentar")

            if (uriFile == null && komentar.isNullOrEmpty()){
                Toast.makeText(applicationContext, "Harap Isi Komentar Ataupun File", Toast.LENGTH_LONG).show()
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    val file: File? = FileUtils.getFile(this@UploadTugasActivity, uriFile)
                    var fileSend = file
                    val extension = nameFile?.substringAfterLast(".")

                    if (extension == "jpg" || extension == "png") {
                        compressedImageFile = Compressor.compress(applicationContext, file!!)
                        fileSend = compressedImageFile
                        Log.d(TAG, "size : ${fileSend?.length()}")
                    }

                    val requestBody: RequestBody? = fileSend!!.asRequestBody("*/*".toMediaTypeOrNull())
                    val multiPartBody: MultipartBody.Part? = requestBody?.let { it1 ->
                        MultipartBody.Part.createFormData("myfile",
                                file?.name ?: "", it1
                        )
                    }

                    val reqNis: RequestBody = (nis
                            ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
                    val reqIdTugas = (data?.id
                            ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
                    val reqKomentar = (komentar
                            ?: "").toRequestBody("text/plain".toMediaTypeOrNull())

                    NetworkConfig.service().uploadTugas(reqIdTugas, reqNis,
                            reqKomentar, multiPartBody)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.d(TAG, "Res : $it")
                                Toast.makeText(applicationContext, "Berhasil Upload Tugas",
                                        Toast.LENGTH_LONG).show()
                                progressDialog?.dismiss()
                                finish()
                            }, {
                                progressDialog?.dismiss()
                                Toast.makeText(applicationContext, "Something Error ! ", Toast.LENGTH_SHORT).show()
                            })
                }
            }
        }
    }

    private fun showDialogVideo() {
        val dialog = AlertDialog.Builder(this)

        view_dialog = layoutInflater.inflate(R.layout.dialog_youtube, null)

        dialog.setView(view_dialog)
        dialogView = dialog.create()
        dialogView = dialog.show()
        dialogView.setCancelable(false)

        val link = data?.linkyt
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

    fun getFileName(uri: Uri?): String? {
        try {
            nameFile = uri?.lastPathSegment
            nameFile?.substring(nameFile?.lastIndexOf("/")?.plus(1)!!)
            binding?.tvNamaFile?.text = nameFile

            Log.d(TAG, "nameFile : $nameFile")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "unknown"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_PICK_FILE && resultCode == Activity.RESULT_OK){
            uriFile = data?.data
            getFileName(uriFile)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view_dialog?.youtube_player_dialog?.release()
        binding = null
        preferences = null
    }
}

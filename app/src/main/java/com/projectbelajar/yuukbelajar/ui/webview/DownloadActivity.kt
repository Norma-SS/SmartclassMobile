package com.projectbelajar.yuukbelajar.ui.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.databinding.ActivityDownloadBinding

class DownloadActivity : AppCompatActivity() {

    private var binding : ActivityDownloadBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.webView?.loadUrl("https://smartclass.co.id/dashboard/uploads/1234_dtortu_yuukbelajar.xls")
    }
}
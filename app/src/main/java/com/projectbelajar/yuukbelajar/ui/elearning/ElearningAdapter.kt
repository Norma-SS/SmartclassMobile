package com.projectbelajar.yuukbelajar.ui.elearning

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.projectbelajar.yuukbelajar.data.network.model.response.ResultBelon
import com.projectbelajar.yuukbelajar.databinding.RowElearningBinding
import com.projectbelajar.yuukbelajar.utils.FullScreenHelper

class ElearningAdapter(val activity: Activity, private val data: List<ResultBelon>, private val lifecycle: Lifecycle) : RecyclerView.Adapter<ElearningAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowElearningBinding) : RecyclerView.ViewHolder(binding.root) {
        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null

        fun cueVideo(data: ResultBelon) {
            currentVideoId = data.link
            if (youTubePlayer == null) return
            youTubePlayer!!.cueVideo(data.link ?: "", 0f)
        }

        fun bind(item: ResultBelon) {
            binding.tvJudul.text = item?.judul
            binding.tvNamaGuru.text = item?.nmguru
            binding.tvWaktuUpload.text = item?.tgl
        }

        init {
            val fullScreenHelper = FullScreenHelper(activity)

            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(initializedYouTubePlayer: YouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer
                    youTubePlayer!!.cueVideo(currentVideoId!!, 0f)
                }
            })
            binding.youtubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    fullScreenHelper.enterFullScreen()
                }

                override fun onYouTubePlayerExitFullScreen() {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    fullScreenHelper.exitFullScreen()
                }
            })
        }
    }

    private fun addFullScreenListenerToPlayer() {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowElearningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        lifecycle.addObserver(binding.youtubePlayerView)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        viewHolder.cueVideo(item)
        viewHolder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}
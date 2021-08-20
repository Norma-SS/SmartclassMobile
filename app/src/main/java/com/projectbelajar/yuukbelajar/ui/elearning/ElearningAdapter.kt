package com.projectbelajar.yuukbelajar.ui.elearning

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.ResultBelon
import com.projectbelajar.yuukbelajar.databinding.RowElearningBinding
import java.text.SimpleDateFormat

class ElearningAdapter(private val data: List<ResultBelon>, val itemClick : OnClick) : RecyclerView.Adapter<ElearningAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowElearningBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResultBelon) {

            if (item?.path.isNullOrEmpty()){
                binding.tvDownload.visibility = GONE
            }

            binding.tvJudul.text = item?.judul

            binding.cardClick.setOnClickListener {
                itemClick.clickItem(item)
            }

            binding.tvDownload.setOnClickListener {
                itemClick.downloadFile(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowElearningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        viewHolder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    interface OnClick{
        fun clickItem(item : ResultBelon)
        fun downloadFile(item: ResultBelon)
    }
}
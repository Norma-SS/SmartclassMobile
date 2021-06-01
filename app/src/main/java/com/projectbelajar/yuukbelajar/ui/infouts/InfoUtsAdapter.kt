package com.projectbelajar.yuukbelajar.ui.infouts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.InfoUtsItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoUtsBinding

class InfoUtsAdapter(val data: List<InfoUtsItem>) : RecyclerView.Adapter<InfoUtsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowInfoUtsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: InfoUtsItem) {
            var isExpand : Boolean = false

            binding.nilaiName.text = item.mpel
            binding.tvNilaiPengetahuan.text = item.nil1
            binding.tvPredikatPengetahuan.text = item.pre1
            binding.tvDeskripsiPengetahuan.text = item.dis1
            binding.tvTgl.text = item.tgl

            binding.tvNilaiKeterampilan.text = item.nil2
            binding.tvPredikatKeterampilan.text = item.pre2
            binding.tvDeskripsiKeterampilan.text = item.dis1

            binding.nilaiName.setOnClickListener {
                if (isExpand){
                    binding.expandableLayout.visibility = GONE
                    isExpand = false

                }else{
                    binding.expandableLayout.visibility = VISIBLE
                    isExpand = true
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoUtsAdapter.ViewHolder {
        return ViewHolder(RowInfoUtsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InfoUtsAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
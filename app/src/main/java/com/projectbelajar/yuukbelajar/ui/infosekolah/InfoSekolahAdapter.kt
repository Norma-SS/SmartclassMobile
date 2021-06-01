package com.projectbelajar.yuukbelajar.ui.infosekolah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.InfoSekolahItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoSekolahBinding

class InfoSekolahAdapter(val data : List<InfoSekolahItem>) : RecyclerView.Adapter<InfoSekolahAdapter.ViewHolder>(){

    class ViewHolder(val binding : RowInfoSekolahBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InfoSekolahItem) {
            binding.tvTgl.text = item.tgl.toString()
            binding.tvDesc.text = item.ket.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoSekolahAdapter.ViewHolder {
        return ViewHolder(RowInfoSekolahBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: InfoSekolahAdapter.ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }
}
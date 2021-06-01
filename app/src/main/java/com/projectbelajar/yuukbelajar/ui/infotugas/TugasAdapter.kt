package com.projectbelajar.yuukbelajar.ui.infotugas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.data.network.model.response.TugasItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoSekolahBinding


class TugasAdapter(val data : List<TugasItem>) : RecyclerView.Adapter<TugasAdapter.ViewHolder>(){


    class ViewHolder(val binding : RowInfoSekolahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TugasItem) {
            binding.imageView5.setImageResource(R.drawable.ic_book)
            binding.tvTgl.text = item.tgl.toString()
            binding.tvDesc.text = item.ket.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasAdapter.ViewHolder {
        return ViewHolder(RowInfoSekolahBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: TugasAdapter.ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }
}
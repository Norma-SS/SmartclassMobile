package com.projectbelajar.yuukbelajar.ui.infosekolah

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.InfoSekolahItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoSekolahBinding
import java.text.SimpleDateFormat

class InfoSekolahAdapter(val data : List<InfoSekolahItem>) : RecyclerView.Adapter<InfoSekolahAdapter.ViewHolder>(){

    class ViewHolder(val binding : RowInfoSekolahBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InfoSekolahItem) {
            binding.tvTgl.text = item.tgl.toString().formatDate()
            binding.tvDesc.text = item.ket.toString()

            binding.tvUnduh.visibility = View.GONE
        }

        @SuppressLint("SimpleDateFormat")
        fun String.formatDate(output : String = "dd MMM yyyy", input : String = "dd-MM-yyyy") : String{
            val inSdf = SimpleDateFormat(input)
            val outSdf = SimpleDateFormat(output)
            val date = inSdf.parse(this)
            return outSdf.format(date)
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
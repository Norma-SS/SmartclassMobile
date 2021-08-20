package com.projectbelajar.yuukbelajar.ui.infouas

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.InfoUasItem
import com.projectbelajar.yuukbelajar.data.network.model.response.InfoUtsItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoUtsBinding
import java.text.SimpleDateFormat

class InfoUasAdapter(val data: List<InfoUasItem>) : RecyclerView.Adapter<InfoUasAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowInfoUtsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: InfoUasItem) {
            var isExpand : Boolean = false

            binding.nilaiName.text = item.mpel
            binding.tvNilaiPengetahuan.text = item.nil1
            binding.tvPredikatPengetahuan.text = item.pre1
            binding.tvDeskripsiPengetahuan.text = item.dis1
            binding.tvTgl.text = item.tgl?.formatDate()

            binding.tvNilaiKeterampilan.text = item.nil2
            binding.tvPredikatKeterampilan.text = item.pre2
            binding.tvDeskripsiKeterampilan.text = item.dis2

            binding.nilaiName.setOnClickListener {
                if (isExpand){
                    binding.expandableLayout.visibility = View.GONE
                    isExpand = false

                }else{
                    binding.expandableLayout.visibility = View.VISIBLE
                    isExpand = true
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun String.formatDate(output : String = "dd MMM yyyy", input : String = "dd-MM-yyyy") : String{
            val inSdf = SimpleDateFormat(input)
            val outSdf = SimpleDateFormat(output)
            val date = inSdf.parse(this)
            return outSdf.format(date)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoUasAdapter.ViewHolder {
        return ViewHolder(RowInfoUtsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InfoUasAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
package com.projectbelajar.yuukbelajar.ui.infoharian

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.QuizHarianItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoHarianBinding
import java.text.SimpleDateFormat

class PenilaianSikapAdapter (val data : List<QuizHarianItem>) : RecyclerView.Adapter<PenilaianSikapAdapter.ViewHolder>(){

    class ViewHolder(val binding : RowInfoHarianBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuizHarianItem) {
            var isExpand : Boolean = false

            binding.tvTgl.text = item?.tgl?.formatDate()

            binding.tvPredikatSpiritual.text = item?.pre1
            binding.tvDeskripsiSpiritual.text = item?.dis1

            binding.tvPredikatSosial.text = item?.pre2
            binding.tvDeskripsiSosial.text = item?.dis2

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenilaianSikapAdapter.ViewHolder {
        return ViewHolder(RowInfoHarianBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PenilaianSikapAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
}
package com.projectbelajar.yuukbelajar.ui.infotugas

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.data.network.model.response.TugasItem
import com.projectbelajar.yuukbelajar.databinding.RowInfoSekolahBinding
import java.text.SimpleDateFormat


class TugasAdapter(val data : List<TugasItem>, val itemClick : OnClick) : RecyclerView.Adapter<TugasAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: RowInfoSekolahBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TugasItem) {
            binding.imageView5.setImageResource(R.drawable.ic_book)
            binding.tvTgl.text = item.tglstart.toString().formatDate() +  " - " + item.tglend.toString().formatDate()
            binding.tvDesc.text = item.deskripsi.toString()

            if (item?.link.isNullOrEmpty()){
                binding.tvUnduh.visibility = INVISIBLE
            }
            binding.tvUnduh.setOnClickListener {
                itemClick.downloadFile(item)
            }

            binding.btnKerjakanTugas.setOnClickListener {
                val intent = Intent(context, UploadTugasActivity::class.java)
                intent.putExtra("data" , item)
                context.startActivity(intent)
            }

        }

        @SuppressLint("SimpleDateFormat")
        fun String.formatDate(output : String = "dd MMM yyyy", input : String = "yyyy-MM-dd") : String{
            val inSdf = SimpleDateFormat(input)
            val outSdf = SimpleDateFormat(output)
            val date = inSdf.parse(this)
            return outSdf.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasAdapter.ViewHolder {
        return ViewHolder(RowInfoSekolahBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                parent.context)
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: TugasAdapter.ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }
    interface OnClick{
        fun downloadFile(item : TugasItem)
    }
}
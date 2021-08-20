package com.projectbelajar.yuukbelajar.ui.ujianonline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.databinding.RowCekUjianBinding

class ReviewUjianAdapter( val index : Int, val hashMap: HashMap<Int, Any>, val itemClick : OnItemClick) : RecyclerView.Adapter<ReviewUjianAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : RowCekUjianBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(position : Int) {
            binding.button.text = "${position.plus(1)}"
            if (hashMap[position] == null){
                binding.textView26.text = "-"
            }else{
                binding.textView26.text = hashMap[position].toString()
            }
            binding.button.setOnClickListener {
                itemClick.klikItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewUjianAdapter.ViewHolder {
        return ViewHolder(RowCekUjianBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = index

    override fun onBindViewHolder(holder: ReviewUjianAdapter.ViewHolder, position: Int) {
       holder.bind(position)
    }
    interface OnItemClick{
        fun klikItem(position: Int)
    }
}


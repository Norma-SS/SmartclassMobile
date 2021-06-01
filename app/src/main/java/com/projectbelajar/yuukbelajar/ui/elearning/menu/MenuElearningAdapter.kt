package com.projectbelajar.yuukbelajar.ui.elearning.menu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.data.network.model.response.MapelItem
import com.projectbelajar.yuukbelajar.databinding.RowMenuElearningBinding
import com.projectbelajar.yuukbelajar.ui.elearning.Elearning

class MenuElearningAdapter (val context : Context, val data : List<MapelItem>) : RecyclerView.Adapter<MenuElearningAdapter.ViewHolder>(){

    inner class ViewHolder(val binding : RowMenuElearningBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MapelItem) {
            binding.imageView4.setImageResource(R.drawable.ic_menu_elearning)
            binding.tvNamaMapel.text = item.mapel
        }

        fun bntListener(item: MapelItem) {
            binding.btnLearning.setOnClickListener {
                val intent = Intent(context, Elearning::class.java)
                intent.putExtra("materi", item.kd)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuElearningAdapter.ViewHolder {
        return ViewHolder(RowMenuElearningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MenuElearningAdapter.ViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)
        holder.bntListener(item)
    }
}
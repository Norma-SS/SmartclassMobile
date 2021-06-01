package com.projectbelajar.yuukbelajar.ui.smartmeet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.ui.smartmeet.model.RoomInfo
import kotlinx.android.synthetic.main.list_meet.view.*

class StudentMeetAdapter (val context: Context, val data : ArrayList<RoomInfo>, val itemClickListener : onItemClick) : RecyclerView.Adapter<StudentMeetAdapter.ViewHolder>() {

      inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

          fun bind(item: RoomInfo, context: Context) {
              view.tv_Meet_nama.text = "Nama Guru : " + item?.nama_guru
              view.tv_Meet_room.text = "Kode Ruangan : " + item?.kode_ruangan

              Glide.with(context)
                      .load(item?.photo_guru)
                      .fitCenter() //menyesuaikan ukuran imageview
                      .crossFade() //animasi
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .into(view.iv_Meet_photo)
              view.setOnClickListener {
                  itemClickListener.join(item)
              }
          }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentMeetAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_meet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: StudentMeetAdapter.ViewHolder, position: Int) {
        holder.bind(data.get(position), context)
    }

    interface onItemClick{
        fun join(item : RoomInfo)
    }
}
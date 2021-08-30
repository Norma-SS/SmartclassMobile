package com.projectbelajar.yuukbelajar.ui.ujianonline

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.projectbelajar.yuukbelajar.data.network.model.response.ResultQuizOnline
import com.projectbelajar.yuukbelajar.databinding.RowQuizonlineBinding
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "QuizOnlineAdapter"
class QuizOnlineAdapter(val context: Activity, val data : List<ResultQuizOnline>) : RecyclerView.Adapter<QuizOnlineAdapter.ViewHolder>(){

    inner class ViewHolder(val binding : RowQuizonlineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultQuizOnline) {

            val date = Calendar.getInstance()
            val formater = SimpleDateFormat("dd/M/yyyy h:mm")
            val currentDate = formater.format(date.time)

            val convertTime = item?.tgl + " " + item?.wkt
            val timeStart = convertTime.formatDate()

            binding.tvNamaMapel.text = item.judul
            binding.tvNamaGuru.text = item.walikls
            binding.tvDurasi.text = item.tempo + " Menit "
            binding.tvMulai.text = item.wkt
            binding.tvTgl.text = item.tgl
            binding.tvNilai.text = item?.nilai

            if (item?.nilai == null)
                binding.tvNilai.text = "Belum Ujian"

            binding.btnMulai.setOnClickListener {

//                if (currentDate.compareTo(timeStart) >= 0){
                    when (item?.nilai) {
                        null -> {
                            val intent = Intent(context, UjianOnline1::class.java)
                            intent.putExtra("soal_id", item.link)
                            context.startActivity(intent)
                            context.finish()
                        }
                        else -> {
                            Toast.makeText(context, "Anda Sudah Melakukan Ujian Ini", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, UjianOnline1::class.java)
                            intent.putExtra("soal_id", item.link)
                            context.startActivity(intent)
                            context.finish()
                        }
                    }
//                }else{
//                    Toast.makeText(context, "Waktu Ujian Belum Berlangsung", Toast.LENGTH_SHORT).show()
//                }
            }
        }
        @SuppressLint("SimpleDateFormat")
        fun String.formatDate(output : String = "dd/M/yyyy h:mm", input : String = "yyyy-MM-dd HH:mm:ss") : String{
            val inSdf = SimpleDateFormat(input)
            val outSdf = SimpleDateFormat(output)
            val date = inSdf.parse(this)
            return outSdf.format(date)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizOnlineAdapter.ViewHolder {
        return ViewHolder(RowQuizonlineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: QuizOnlineAdapter.ViewHolder, position: Int) {

        val item = data[position]

        holder.bind(item)
    }
}
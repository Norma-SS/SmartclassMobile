package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAcakSoal(

        @field:SerializedName("data")
        val data: List<AcakSoalItem?>? = null,

        @field:SerializedName("isSuccess")
        val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class AcakSoalItem(


        @field:SerializedName("ketsoal")
        val ketSoal: String? = null,

        @field:SerializedName("link")
        val link: String? = null,

        @field:SerializedName("a")
        val A: String? = null,

        @field:SerializedName("b")
        val B: String? = null,

        @field:SerializedName("c")
        val C: String? = null,

        @field:SerializedName("d")
        val D: String? = null,

        @field:SerializedName("e")
        val E: String? = null,

        @field:SerializedName("soal_id")
        val soalId: String? = null,

        @field:SerializedName("mapel")
        val mapel: String? = null,

        @field:SerializedName("nosoal")
        val nosoal: String? = null,

        @field:SerializedName("jumlahSoal")
        val jumlahSoal: Int? = null,

        @field:SerializedName("jwban")
        val jwban: String? = null,

        @field:SerializedName("soal")
        val soal: String? = null,

        @field:SerializedName("nmskl")
        val nmskl: String? = null,

        @field:SerializedName("jdluji")
        val jdluji: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("durasi")
        val durasi: Int? = null
) : Parcelable

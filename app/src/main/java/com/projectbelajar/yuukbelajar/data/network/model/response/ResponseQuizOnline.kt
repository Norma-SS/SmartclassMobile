package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseQuizOnline(

        @field:SerializedName("result")
        val result: List<ResultQuizOnline>? = null
) : Parcelable

@Parcelize
data class ResultQuizOnline(

        @field:SerializedName("isSuccess")
        val isSuccess: Boolean? = null,

        @field:SerializedName("wkt")
        val wkt: String? = null,

        @field:SerializedName("kls")
        val kls: String? = null,

        @field:SerializedName("nmskl")
        val nmskl: String? = null,

        @field:SerializedName("walikls")
        val walikls: String? = null,

        @field:SerializedName("link")
        val link: String? = null,

        @field:SerializedName("tgl")
        val tgl: String? = null,

        @field:SerializedName("tempo")
        val tempo: String? = null,

        @field:SerializedName("mulai")
        val mulai: Int? = null,

        @field:SerializedName("cekujian")
        val cekujian: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("judul")
        val judul: String? = null,

        @field:SerializedName("status2")
        val status2: String? = null,

        @field:SerializedName("nilai")
		val nilai: String? = null
) : Parcelable

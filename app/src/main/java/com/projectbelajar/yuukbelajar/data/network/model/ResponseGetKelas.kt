package com.projectbelajar.yuukbelajar.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetKelas(

	@field:SerializedName("result")
	val result: Result? = null
) : Parcelable

@Parcelize
data class DatanyaItem(

	@field:SerializedName("kelas")
	val kelas: String? = null
) : Parcelable

@Parcelize
data class Result(

	@field:SerializedName("datanya")
	val datanya: List<DatanyaItem?>? = null,

	@field:SerializedName("kodeskl")
	val kodeskl: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null
) : Parcelable

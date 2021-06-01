package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetInfoSekolah(

	@field:SerializedName("result")
	val result: List<InfoSekolahItem>? = null
) : Parcelable

@Parcelize
data class InfoSekolahItem(

	@field:SerializedName("kls")
	val kls: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("nmssw")
	val nmssw: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null
) : Parcelable

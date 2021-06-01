package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetInfoTugas(

	@field:SerializedName("result")
	val result: List<TugasItem>? = null
) : Parcelable

@Parcelize
data class TugasItem(

	@field:SerializedName("kls")
	val kls: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("nmssw")
	val nmssw: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null
) : Parcelable

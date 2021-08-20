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


	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tglstart")
	val tglstart: String? = null,

	@field:SerializedName("tglend")
	val tglend: String? = null,

	@field:SerializedName("namaguru")
	val namaguru: String? = null,

	@field:SerializedName("wktstart")
	val wktstart: String? = null,

	@field:SerializedName("wktend")
	val wktend: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("linkyt")
	val linkyt: String? = null




) : Parcelable

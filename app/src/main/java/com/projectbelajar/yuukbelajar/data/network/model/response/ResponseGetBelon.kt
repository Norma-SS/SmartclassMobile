package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetBelon(

	@field:SerializedName("result")
	val resultBelon : List<ResultBelon>? = null
) : Parcelable

@Parcelize
data class ResultBelon(

	@field:SerializedName("wkt")
	val wkt: String? = null,

	@field:SerializedName("kls")
	val kls: String? = null,

	@field:SerializedName("nmskl")
	val nmskl: String? = null,

	@field:SerializedName("nmguru")
	val nmguru: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("status2")
	val status2: String? = null,

	@field:SerializedName("nmmapel")
	val nmmapel: String? = null
) : Parcelable

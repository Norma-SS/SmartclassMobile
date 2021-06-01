package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetMapel(

	@field:SerializedName("result")
	val result: ResultMapel? = null
) : Parcelable

@Parcelize
data class MapelItem(

	@field:SerializedName("kd")
	val kd: String? = null,

	@field:SerializedName("mapel")
	val mapel: String? = null
) : Parcelable

@Parcelize
data class ResultMapel(

        @field:SerializedName("datanya")
	val datanya: List<MapelItem>? = null,

        @field:SerializedName("kodeskl")
	val kodeskl: String? = null,

        @field:SerializedName("ket")
	val ket: String? = null
) : Parcelable

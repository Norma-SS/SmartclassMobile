package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAbsensi(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null
) : Parcelable

@Parcelize
data class ResultItem(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

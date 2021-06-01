package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetQuizHarian(

	@field:SerializedName("result")
	val result: List<QuizHarianResult?>? = null
) : Parcelable

@Parcelize
data class QuizHarianResult(

	@field:SerializedName("kls")
	val kls: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("nmssw")
	val nmssw: String? = null,

	@field:SerializedName("pre1")
	val pre1: String? = null,

	@field:SerializedName("pre2")
	val pre2: String? = null,

	@field:SerializedName("dis2")
	val dis2: String? = null,

	@field:SerializedName("dis1")
	val dis1: String? = null
) : Parcelable

package com.projectbelajar.yuukbelajar.data.network.model.response

import com.google.gson.annotations.SerializedName

data class ResponseScore(

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

package com.projectbelajar.yuukbelajar.data.network.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseUploadTugas(

        @field:SerializedName("result")
        val result: ResultUploadTugas? = null
) : Parcelable

@Parcelize
data class ResultUploadTugas(


        @field: SerializedName("isUpload")
        val isUpload: String? = null,

        @field: SerializedName("path")
        val path: String? = null,

        @field:SerializedName("msg")
        val msg: String? = null,

        @field:SerializedName("isSuccess")
        val isSuccess: Boolean? = null
) : Parcelable

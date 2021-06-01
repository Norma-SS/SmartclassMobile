package com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student

import com.google.gson.annotations.SerializedName

data class RequestCheckOut (

        @field:SerializedName("id")
        val id : String ?= null,

        @field:SerializedName("check_out")
        val check_out : String ?= null
)
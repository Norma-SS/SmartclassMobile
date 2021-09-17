package com.projectbelajar.yuukbelajar.data.network.model.request.ujian

import com.google.gson.annotations.SerializedName

data class RequestDoneExam (

        @field:SerializedName("kdsoal")
        val nip : String ?= null,

        @field:SerializedName("email")
        val email : String ?= null,

        @field:SerializedName("kdskl")
        val kdskl : String ?= null
)
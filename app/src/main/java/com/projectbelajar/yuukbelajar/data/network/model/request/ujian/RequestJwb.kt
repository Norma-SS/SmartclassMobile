package com.projectbelajar.yuukbelajar.data.network.model.request.ujian

import com.google.gson.annotations.SerializedName

data class RequestJwb (

        @field:SerializedName("eml")
        val eml : String ?= null,

        @field:SerializedName("kdsoal")
        val kdsoal : String ?= null,

        @field:SerializedName("nourut")
        val nourut : String ?= null,

        @field:SerializedName("nosoal")
        val nosoal : String ?= null,

        @field:SerializedName("jwbn")
        val jwbn: String? = null

)
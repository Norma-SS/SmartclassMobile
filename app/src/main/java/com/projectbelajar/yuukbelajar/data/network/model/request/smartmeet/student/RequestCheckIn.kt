package com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student

import com.google.gson.annotations.SerializedName

data class RequestCheckIn (

        @field:SerializedName("nis")
        val nis : String ?= null,

        @field:SerializedName("nama")
        val nama : String ?= null,

        @field:SerializedName("kode_sekolah")
        val kode_sekolah : String ?= null,

        @field:SerializedName("kelas")
        val kelas : String ?= null,

        @field:SerializedName("check_in")
        val check_in : String ?= null,

        @field:SerializedName("check_out")
        val check_out : String ?= null,

        @field:SerializedName("kode_ruangan")
        val kode_ruangan : String ?= null
)

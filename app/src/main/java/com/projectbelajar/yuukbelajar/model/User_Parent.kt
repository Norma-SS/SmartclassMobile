package com.projectbelajar.yuukbelajar.model

data class User_Parent(
    var long : Double ?= 0.0,
    var lat : Double ?= 0.0,
    var email : String ?= null,
    var id : String ?= null,
    var imgUrl : String?= null,
    var kelas : String ?= null,
    var kodeSekolah : String ?= null,
    var level : String?= null,
    var nama : String ?= null,
    var namaSekolah : String ?= null,
    var nis : String ?= null,
    var password : String ?= null,
    var sort_nama : String ?= null,
    var status : String ?= null,
    var time : String ?= null,
    var username : String ?= null
)
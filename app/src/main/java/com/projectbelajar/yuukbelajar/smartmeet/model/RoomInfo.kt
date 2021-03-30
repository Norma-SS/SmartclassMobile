package com.projectbelajar.yuukbelajar.smartmeet.model

data class RoomInfo(val id : String ?= null,
                    val kode_ruangan : String ?= null,
                    val kelas : String ?= null,
                    val nama_guru : String ?= null,
                    val photo_guru : String ?= null)
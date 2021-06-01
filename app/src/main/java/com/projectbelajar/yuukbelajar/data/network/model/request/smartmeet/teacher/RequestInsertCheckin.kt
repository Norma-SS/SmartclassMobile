package com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.teacher

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestInsertCheckin(

	@field:SerializedName("check_out")
	val checkOut: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("kode_sekolah")
	val kodeSekolah: String? = null,

	@field:SerializedName("kelas")
	val kelas: String? = null,

	@field:SerializedName("check_in")
	val checkIn: String? = null,

	@field:SerializedName("kode_ruangan")
	val kodeRuangan: String? = null
) : Parcelable

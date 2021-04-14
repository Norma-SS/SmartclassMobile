package com.projectbelajar.yuukbelajar.data.network

import com.projectbelajar.yuukbelajar.data.network.model.ResponseAction
import com.projectbelajar.yuukbelajar.data.network.model.ResponseGetKelas
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {

    @FormUrlEncoded
    @POST("insert.php")
    fun insert_chec_in(
            @Field("id") id : String,
            @Field("nis") nis: String,
            @Field("nama") nama: String,
            @Field("kode_sekolah") kode_sekolah: String,
            @Field("kelas") kelas : String,
            @Field("check_in") check_in: String
    ): Single<ResponseAction>

    @FormUrlEncoded
    @POST("update.php")
    fun insert_check_out(
            @Field("id") id: String,
            @Field("check_out") check_out: String
    ) : Single<ResponseAction>


    @FormUrlEncoded
    @POST("dataKelas.php")
    fun get_list_kelas(@Field("kdskl") kdskl : String

    ) : Flowable<ResponseGetKelas>

}
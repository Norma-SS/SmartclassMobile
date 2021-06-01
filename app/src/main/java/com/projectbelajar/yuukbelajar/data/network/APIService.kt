package com.projectbelajar.yuukbelajar.data.network

import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckIn
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckOut
import com.projectbelajar.yuukbelajar.data.network.model.response.*
import com.projectbelajar.yuukbelajar.data.network.model.response.ResponseAction
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface APIService {

    @POST("insert.php")
    fun insert_chec_in(
          @Body req : RequestCheckIn
    ): Single<ResponseAction>


    @POST("update.php")
    fun insert_check_out(
           @Body req: RequestCheckOut
    ) : Single<ResponseAction>


    @FormUrlEncoded
    @POST("dashboard/dataKelas.php")
    fun get_list_kelas(@Field("kdskl") kdskl : String

    ) : Flowable<ResponseGetKelas>

    @GET("infosekolah.php")
    fun getInfoSekolah(
            @Query("eml") email : String
    ) : Flowable<ResponseGetInfoSekolah>

    @GET("infouts.php")
    fun getInfoUts(
            @Query("eml") email : String
    ) : Flowable<ResponseGetInfoUts>

    @GET("infouas.php")
    fun getInfoUas(
            @Query("eml") email : String
    ) : Flowable<ResponseGetInfoUas>

    @GET("infoquiz.php")
    fun getInfoQuiz(

    )

    @GET("infotugas.php")
    fun getInfoTugas(
            @Query("eml") email : String
    ) : Flowable<ResponseGetInfoTugas>

    @GET("belon.php")
    fun getBelon(

            @Query("jurusan") jurusan : String,
            @Query("kdmapel") kdmapel : String,
            @Query("kdskl") kdskl : String

    ) : Flowable<ResponseGetBelon>


    @GET("dashboard/dataMapel.php")
    fun getdataMapel(
            @Query("kdskl") kdskl : String
    ) : Flowable<ResponseGetMapel>
}
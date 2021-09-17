package com.projectbelajar.yuukbelajar.data.network

import com.projectbelajar.yuukbelajar.data.network.model.request.ujian.RequestDoneExam
import com.projectbelajar.yuukbelajar.data.network.model.request.elearning.RequestElearning
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckIn
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckOut
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.teacher.RequestInsertCheckin
import com.projectbelajar.yuukbelajar.data.network.model.request.ujian.RequestJwb
import com.projectbelajar.yuukbelajar.data.network.model.response.*
import com.projectbelajar.yuukbelajar.data.network.model.response.ResponseAction
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @POST("mobile/api/product/done.php")
    fun hitDone(
            @Body req: RequestDoneExam
    ) : Single<ResponseAction>

    @FormUrlEncoded
    @POST("absensi.php")
    fun insertAbsenSiswa(
        @Field("email") email : String,
        @Field("ket") ket : String
    ) : Flowable<ResponseAbsensi>

    @POST("e_learn.php")
    fun insert_absent_elearning(
            @Body req: RequestElearning
    ) : Single<ResponseAction>

    @POST("insert.php")
    fun insert_chec_in(
          @Body req : RequestCheckIn
    ): Single<ResponseAction>


    @POST("insert_checkout.php")
    fun insert_check_out(
           @Body req: RequestCheckOut
    ) : Single<ResponseAction>

    @POST("insert_absguru.php")
    fun insert_check_in_guru(
            @Body req : RequestInsertCheckin
    ) : Single<ResponseAction>

    @POST("insert_checkout_guru.php")
    fun insert_check_out_guru(
            @Body req : RequestCheckOut
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
        @Query("eml") email : String
    ) : Flowable<ResponseGetQuizHarian>

    @Multipart
    @POST("upload_tugas.php")
    fun uploadTugas(@Part("id") id: RequestBody,
                    @Part("nis") nis: RequestBody,
                    @Part("komen") komen: RequestBody,
                    @Part myfile: MultipartBody.Part?= null
    ) : Single<ResponseUploadTugas>

    @GET("info_tugas.php")
    fun getInfoTugas(
            @Query("eml") email : String
    ) : Flowable<ResponseGetInfoTugas>

    @GET("belon.php")
    fun getBelon(

            @Query("kelas") jurusan : String,
            @Query("kdmapel") kdmapel : String,
            @Query("kdskl") kdskl : String

    ) : Flowable<ResponseGetBelon>


    @GET("dashboard/dataMapel.php")
    fun getdataMapel(
            @Query("kdskl") kdskl : String
    ) : Flowable<ResponseGetMapel>

    @GET("quizon.php")
    fun getQuiz(
            @Query("eml") eml : String
    ) : Flowable<ResponseQuizOnline>

    @FormUrlEncoded
    @POST("acaksoal.php")
    fun acakSoal(
            @Field("emlx") emlx : String,
            @Field("kdsoalx") kdsoalx : String
    ) : Flowable<ResponseAcakSoal>


    @POST("mobile/api/product/jwbanx.php")
    fun saveJawaban(
        @Body req : RequestJwb
    ) : Single<ResponseAction>

    @GET("score.php")
    fun getScore(
            @Query("kdskl") kdskl : String,
            @Query("kdsoal") kdsoal : String,
            @Query("email") eml : String
    ) : Single<ResponseScore>
}
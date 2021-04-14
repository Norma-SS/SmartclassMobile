package com.projectbelajar.yuukbelajar.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory



object NetworkConfig {

    fun interceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        return client
    }

    fun service_absen() :  APIService{
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.43.242/smartmeet/")
                .client(interceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

        return retrofit.create(APIService::class.java)
    }

    fun service_kelas() : APIService{
        val retrofit = Retrofit.Builder()
                .baseUrl("https://smartclass.co.id/dashboard/")
                .client(interceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

        return retrofit.create(APIService::class.java)
    }

}
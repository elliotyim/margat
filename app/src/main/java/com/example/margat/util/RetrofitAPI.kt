package com.example.margat.util

import com.example.margat.config.WebConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitAPI: WebConfig() {

    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    val creater: Retrofit = Retrofit.Builder()
        .baseUrl("${ipAddress}${portNo}")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(createOkHttpClient())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.addInterceptor(interceptor)
        builder.connectTimeout(5, TimeUnit.SECONDS)
        builder.readTimeout(5, TimeUnit.SECONDS)

        return builder.build()
    }
}
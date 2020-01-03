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
    
    companion object {
        private var instance: RetrofitAPI = RetrofitAPI()
        fun newInstance(): RetrofitAPI = instance
    }

    fun getRetrofit(): Retrofit = retrofit

    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("${ipAddress}${portNo}")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(createOkHttpClient())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder().apply{
            addInterceptor(interceptor)
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
        }
        return builder.build()
    }


}

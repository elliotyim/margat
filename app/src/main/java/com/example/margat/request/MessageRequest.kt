package com.example.margat.request

import com.example.margat.model.MyMessageItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MessageRequest {
    @GET("/messages/list/{memNo}")
    fun findMessageList(@Path("memNo") memberNo:Int): Call<ArrayList<MyMessageItem.MessageItem>>
}
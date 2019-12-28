package com.example.margat.controller

import com.example.margat.domain.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostingController {
    @GET("posts/member/{no}")
    fun findAllPostsOf(@Path("no") no:Int): Call<Array<Post>>
}
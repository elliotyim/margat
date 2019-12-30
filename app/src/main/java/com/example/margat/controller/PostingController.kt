package com.example.margat.controller

import com.example.margat.domain.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PostingController {
    @GET("posts/member/{no}")
    fun findAllPostsOf(@Path("no") memberNo:Int): Call<Array<Post>>

    @Multipart
    @POST("post/photos")
    fun writePostWithPhotos(
        @PartMap map: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part>
        ): Call<ResponseBody>

}
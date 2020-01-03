package com.example.margat.request

import com.example.margat.model.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PostingRequest {
    @GET("/posts/member/{no}")
    fun findAllPostsOf(@Path("no") memberNo:Int): Call<ArrayList<Post>>

    @Multipart
    @POST("/post/photos")
    fun writePostWithPhotos(
        @PartMap map: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part>
        ): Call<ResponseBody>

}
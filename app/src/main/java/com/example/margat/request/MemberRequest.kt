package com.example.margat.request

import com.example.margat.domain.Member
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MemberRequest {
    @POST("member/email")
    fun findMemberByEmail(@Body member: Member): Call<Array<Member>>

    @POST("member/email/password")
    fun findMemberByEmailAndPassword(@Body member: Member): Call<Array<Member>>

    @POST("member/name/email")
    fun findMemberByNameAndEmail(@Body member: Member): Call<Array<Member>>

    @PATCH("member/password")
    fun sendRandomPasswordAt(@Body member: Member): Call<ResponseBody>

    @POST("member/registration")
    fun insert(@Body member: Member): Call<ResponseBody>
}

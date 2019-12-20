package com.example.margat.service

import com.example.margat.domain.Member
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST("member/email/password")
    fun findMemberByEmailAndPassword(@Body member: Member): Call<Array<Member>>

    @POST("member/registration")
    fun insert(@Body member: Member): Call<Array<Member>>
}

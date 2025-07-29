package com.example.zippick.network.member

import com.example.zippick.ui.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {
    @POST("/api/members/signup")
    suspend fun signUp(@Body request: SignUpRequest): retrofit2.Response<Void>

    @GET("/api/members/check-email")
    suspend fun checkEmail(@Query("email") email: String): retrofit2.Response<Boolean>
}

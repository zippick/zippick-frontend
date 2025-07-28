package com.example.zippick.network.fcm

import com.example.zippick.network.fcm.dto.FcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmApi {
    @Headers("Content-Type: application/json")
    @POST("api/fcm/register")
    suspend fun registerToken(@Body request: FcmTokenRequest): FcmTokenResponse
}

data class FcmTokenResponse(
    val success: Boolean
)

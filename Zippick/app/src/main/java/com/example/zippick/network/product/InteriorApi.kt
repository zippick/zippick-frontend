package com.example.zippick.network.product

import com.example.zippick.ui.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface InteriorApi {
    @Multipart
    @POST("api/products/ai-interior")
    suspend fun postAiInterior(
        @Part roomImage: MultipartBody.Part,
    ): AiInteriorResponse
}
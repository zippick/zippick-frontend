package com.example.zippick.network.product

import com.example.zippick.ui.model.AiInteriorResponse
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.model.RecommendRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface InteriorApi {
    @Multipart
    @POST("api/products/ai-interior")
    suspend fun postAiInterior(
        @Part roomImage: MultipartBody.Part,
    ): AiInteriorResponse


    @POST("api/products/recommend")
    suspend fun postRecommendProduct(
        @Body request: RecommendRequest
    ): List<Product>
}
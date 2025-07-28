package com.example.zippick.network.product

import com.example.zippick.network.RetrofitInstance
import com.example.zippick.ui.model.*
import okhttp3.MultipartBody

object InteriorRepository {
    private val api = RetrofitInstance.retrofit.create(InteriorApi::class.java)

    suspend fun postAiInterior(
        roomImage: MultipartBody.Part
    ): AiInteriorResponse {
        return api.postAiInterior(roomImage)
    }

    suspend fun postRecommendProduct(
        request: RecommendRequest
    ): List<Product> {
        return api.postRecommendProduct(request)
    }
}
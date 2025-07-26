package com.example.zippick.network.product

import com.example.zippick.ui.model.*
import retrofit2.http.*

interface ProductApi {

    // 사이즈 기반 목록 조회
    @GET("api/products")
    suspend fun getProductsBySize(
        @Query("width") width: Int,
        @Query("depth") depth: Int,
        @Query("height") height: Int,
        @Query("sort") sort: String,
        @Query("offset") offset: Int
    ): ProductResponse

    // 키워드 기반 목록 조회
    @GET("api/products")
    suspend fun getProductsByKeyword(
        @Query("keyword") keyword: String,
        @Query("sort") sort: String,
        @Query("offset") offset: Int
    ): ProductResponse

    // AI 가구 배치 (이미지 URL 반환)
    @POST("api/products/ai-layout")
    suspend fun postAiLayout(
        @Body request: AiLayoutRequest
    ): AiLayoutImageResponse
}

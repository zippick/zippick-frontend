package com.example.zippick.network.product

import com.example.zippick.ui.model.AiLayoutImageResponse
import com.example.zippick.ui.model.LikedRequest
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.model.ProductDetail
import com.example.zippick.ui.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    // 사이즈 기반 목록 조회
    @GET("api/products")
    suspend fun getProductsBySize(
        @Query("category") category: String,
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

    // 카테고리 & 가격 기반 목록 조회
    @GET("api/products")
    suspend fun getProductsByCategoryAndPrice(
        @Query("category") category: String,
        @Query("min_price") minPrice: Long?,
        @Query("max_price") maxPrice: Long?,
        @Query("sort") sort: String,
        @Query("offset") offset: Int
    ): ProductResponse

    // AI 가구 배치 (이미지 URL 반환)
    @Multipart
    @POST("api/products/ai-layout")
    suspend fun postAiLayout(
        @Part roomImage: MultipartBody.Part,
        @Part("furnitureImageUrl") furnitureImageUrl: RequestBody,
        @Part("category") category: RequestBody
    ): AiLayoutImageResponse

    // 찜 목록 리스트 조회
    @POST("api/products/liked")
    suspend fun getLikedProducts(
        @Body request: LikedRequest
    ): List<Product>

    // 상품 상세 조회
    @GET("api/products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): ProductDetail
}

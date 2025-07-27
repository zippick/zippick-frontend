package com.example.zippick.network.product

import com.example.zippick.network.RetrofitInstance
import com.example.zippick.ui.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductRepository {
    private val api = RetrofitInstance.retrofit.create(ProductApi::class.java)

    suspend fun getProductsBySize(
        category: String,
        width: Int,
        depth: Int,
        height: Int,
        sort: String,
        offset: Int
    ): ProductResponse {
        return api.getProductsBySize(category, width, depth, height, sort, offset)
    }

    suspend fun getProductsByKeyword(
        keyword: String,
        sort: String,
        offset: Int
    ): ProductResponse {
        return api.getProductsByKeyword(keyword, sort, offset)
    }

    suspend fun postAiLayout(
        roomImage: MultipartBody.Part,
        furnitureImageUrl: RequestBody,
        category: RequestBody
    ): AiLayoutImageResponse {
        return api.postAiLayout(roomImage, furnitureImageUrl, category)
    }

}
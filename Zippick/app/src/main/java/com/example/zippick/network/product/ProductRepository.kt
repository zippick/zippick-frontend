package com.example.zippick.network.product

import com.example.zippick.network.RetrofitInstance
import com.example.zippick.ui.model.*

class ProductRepository {
    private val api = RetrofitInstance.retrofit.create(ProductApi::class.java)

    suspend fun getProductsBySize(
        width: Int,
        depth: Int,
        height: Int,
        sort: String,
        offset: Int
    ): ProductResponse {
        return api.getProductsBySize(width, depth, height, sort, offset)
    }

    suspend fun getProductsByKeyword(
        keyword: String,
        sort: String,
        offset: Int
    ): ProductResponse {
        return api.getProductsByKeyword(keyword, sort, offset)
    }

    suspend fun postAiLayout(request: AiLayoutRequest): AiLayoutImageResponse {
        return api.postAiLayout(request)
    }
}

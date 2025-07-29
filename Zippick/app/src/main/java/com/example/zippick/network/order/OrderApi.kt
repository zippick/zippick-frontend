package com.example.zippick.network.order;

import com.example.zippick.ui.model.OrderRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface OrderApi {
    @POST("order")
    suspend fun postOrder(@Body request: OrderRequest): Response<Unit>
}
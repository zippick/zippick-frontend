package com.example.zippick.network.order;

import com.example.zippick.ui.model.OrderRequest
import com.example.zippick.ui.model.OrderResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi {
    @POST("api/order/save")
    suspend fun postOrder(
            @Body request: OrderRequest
    ): OrderResponse
}
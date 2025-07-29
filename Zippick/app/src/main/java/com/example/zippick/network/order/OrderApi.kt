package com.example.zippick.network.order;

import com.example.zippick.ui.model.OrderDetailResponse
import com.example.zippick.ui.model.OrderRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OrderApi {
    @POST("api/order/save")
    suspend fun postOrder(@Body request: OrderRequest): Response<Unit>

    @GET("api/order/detail/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: Int,
        @Header("token") token: String
    ): Response<OrderDetailResponse>
}
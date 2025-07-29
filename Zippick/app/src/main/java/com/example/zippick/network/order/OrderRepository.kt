package com.example.zippick.network.order;

import com.example.zippick.network.RetrofitInstance
import com.example.zippick.ui.model.OrderRequest
import com.example.zippick.ui.model.OrderResponse
import retrofit2.Response

class OrderRepository {
    private val api = RetrofitInstance.retrofit.create(OrderApi::class.java)

    suspend fun postOrder(request: OrderRequest): Response<Unit> {
        return api.postOrder(request)
    }
}
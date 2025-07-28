package com.example.zippick.network.order;

import com.example.zippick.network.RetrofitInstance
import com.example.zippick.ui.model.OrderRequest
import com.example.zippick.ui.model.OrderResponse

class OrderRepository {
    private val api = RetrofitInstance.retrofit.create(OrderApi::class.java)

    suspend fun postOrder(request: OrderRequest): OrderResponse {
        return api.postOrder(request)
    }
}
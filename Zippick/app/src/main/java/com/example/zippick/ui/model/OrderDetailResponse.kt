package com.example.zippick.ui.model

data class OrderDetailResponse(
    val orderDate: String,
    val orderNumber: String,
    val name: String,
    val address: String,
    val productId: Int,
    val productName: String,
    val thumbnailImageUrl: String,
    val count: Int,
    val price: Int,
    val totalPrice: Int
)
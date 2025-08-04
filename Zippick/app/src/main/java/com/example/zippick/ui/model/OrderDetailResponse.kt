package com.example.zippick.ui.model

data class OrderDetailResponse(
    val memberName: String,
    val basicAddress: String,
    val detailAddress: String,
    val productId: Int,
    val productImageUrl: String,
    val productName: String,
    val productPrice: Int,
    val createdAt: String,
    val orderId: Int,
    val merchantOrderId: String,
    val count: Int,
    val totalPrice: Int,
    val status: String
)

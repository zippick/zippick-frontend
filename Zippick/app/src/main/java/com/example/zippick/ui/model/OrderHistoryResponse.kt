package com.example.zippick.ui.model

data class OrderHistoryResponse(
    val id: Int,
    val createdAt: String,
    val merchantOrderId: String,
    val productName: String,
    val status: String,
    val productImageUrl: String
)
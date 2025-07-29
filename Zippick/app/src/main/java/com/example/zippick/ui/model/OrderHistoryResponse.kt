package com.example.zippick.ui.model

data class OrderHistoryResponse(
    val id: Int,
    val createdAt: String, // 또는 LocalDateTime 대신 String으로 받기
    val merchantOrderId: String,
    val productName: String,
    val status: String,
    val productImageUrl: String
)
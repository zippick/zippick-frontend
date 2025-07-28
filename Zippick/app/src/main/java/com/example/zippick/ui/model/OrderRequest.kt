package com.example.zippick.ui.model;

data class OrderRequest(
        val totalPrice: Int,
        val count: Int,
        val merchantOrderId: String,
        val productId: Long
)
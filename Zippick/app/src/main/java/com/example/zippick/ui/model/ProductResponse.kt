package com.example.zippick.ui.model

data class ProductResponse(
    val products: List<Product>,
    val totalCount: Int
)
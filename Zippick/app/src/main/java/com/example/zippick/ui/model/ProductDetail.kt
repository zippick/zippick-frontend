package com.example.zippick.ui.model

data class ProductDetail(
    val id : Int,
    val name: String,
    val price: Int,
    val mainImageUrl: String,
    val width: Int,
    val depth: Int,
    val height: Int,
    val color: String,
    val style: String,
    val category: String,
    val detailImage: String
)
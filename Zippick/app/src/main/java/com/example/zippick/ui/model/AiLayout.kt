package com.example.zippick.ui.model

import kotlinx.serialization.Serializable

data class AiLayoutRequest(
    val furnitureImageUrl: String,
    val category: String
)

data class AiLayoutImageResponse(
    val resultImageUrl: String
)

@Serializable
data class AiLayoutProduct(
    val name: String,
    val price: Int,
    val category: String,
    val imageUrl: String
)


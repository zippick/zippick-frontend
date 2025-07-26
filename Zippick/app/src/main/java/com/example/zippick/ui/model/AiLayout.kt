package com.example.zippick.ui.model

data class AiLayoutRequest(
    val furnitureImageUrl: String,
    val category: String
)

data class AiLayoutImageResponse(
    val resultImageUrl: String
)

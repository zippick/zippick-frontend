package com.example.zippick.ui.model.dummy

data class AiCombineRequest(
    val furnitureImageUrl: String,
    val category: String
)

data class AiCombineResponse(
    val resultImageUrl: String
)

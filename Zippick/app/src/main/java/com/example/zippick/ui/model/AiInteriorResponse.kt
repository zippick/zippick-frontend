package com.example.zippick.ui.model

data class AiInteriorResponse(
    val palette: List<PaletteColor>,
    val tags: List<String>
)

data class PaletteColor(
    val colorCode: String,
    val colorName: String,
    val toneCategory: String
)
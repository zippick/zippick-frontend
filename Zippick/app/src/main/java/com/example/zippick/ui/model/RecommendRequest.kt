package com.example.zippick.ui.model

data class RecommendRequest(
    val category: String,
    val recommendType: String,
    val toneCategories: List<String>,
    val tags: List<String>
)
package com.example.zippick.ui.model

enum class SortOption(val label: String, val code: String) {
    LATEST("최신순(기본)", "latest"),
    POPULAR("인기순", "popular"),
    LOW_PRICE("낮은 가격순", "price_asc"),
    HIGH_PRICE("높은 가격순", "price_desc")
}

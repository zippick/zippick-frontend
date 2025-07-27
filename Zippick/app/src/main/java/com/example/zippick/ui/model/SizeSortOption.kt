package com.example.zippick.ui.model

enum class SizeSortOption(val label: String, val value: String) {
    NEWEST("최신순", "created_at_desc"),
    POPULAR("인기순", "popularity_desc"),
    LOW_PRICE("낮은 가격순", "price_asc"),
    HIGH_PRICE("높은 가격순", "price_desc")
}

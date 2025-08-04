package com.example.zippick.util

fun convertToParam(korean: String): String {
    return when (korean.trim()) {
        "의자" -> "chair"
        "책상" -> "desk"
        "소파" -> "sofa"
        "식탁" -> "table"
        "옷장" -> "closet"
        "침대" -> "bed"
        "전체" -> "all"
        else -> "all"
    }
}

fun convertToKorean(english: String): String {
    return when (english.trim().lowercase()) {
        "chair" -> "의자"
        "desk" -> "책상"
        "sofa" -> "소파"
        "table" -> "식탁"
        "closet" -> "옷장"
        "bed" -> "침대"
        "all" -> "전체"
        else -> "전체"
    }
}

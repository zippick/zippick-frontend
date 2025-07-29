package com.example.zippick.ui.model

data class SignUpRequest(
    val loginId: String,
    val password: String,
    val name: String,
    val zipcode: String,
    val basicAddress: String,
    val detailAddress: String
)

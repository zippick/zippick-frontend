package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.zippick.ui.composable.detail.ProductDetailContent
import com.example.zippick.ui.model.dummy.dummyProductDetail

@Composable
fun DetailScreen(
    navController: NavController,
    itemId: String
) {
    val product = dummyProductDetail

    ProductDetailContent(product, navController)
}
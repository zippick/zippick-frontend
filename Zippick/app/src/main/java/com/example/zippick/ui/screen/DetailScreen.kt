package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zippick.ui.composable.detail.ProductDetailContent
import com.example.zippick.ui.viewmodel.ProductDetailViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    itemId: String
) {
    val viewModel: ProductDetailViewModel = viewModel()

    val product = viewModel.productDetail.value
    val intItemId = itemId.toIntOrNull()

    LaunchedEffect(intItemId) {
        intItemId?.let { viewModel.loadProductDetail(it) }
    }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        ProductDetailContent(product = product, navController = navController)
    }
}

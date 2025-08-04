package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zippick.ui.composable.detail.ProductDetailContent
import com.example.zippick.ui.composable.photo.LottieLoading
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
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieLoading(modifier = Modifier.size(90.dp))
            }
        }
    } else {
        ProductDetailContent(product = product, navController = navController)
    }
}

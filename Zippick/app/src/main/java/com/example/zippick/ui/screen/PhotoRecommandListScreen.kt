package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.composable.category.CompareFloatingButton
import com.example.zippick.ui.viewmodel.PhotoRecommendViewModel

@Composable
fun PhotoRecommandListScreen(
    navController: NavController,
    category: String,  // ex: "소파"
    type: String,      // ex: "color" or "style"
    values: String     // ex: "우드톤,뉴트럴"
) {
    val viewModel: PhotoRecommendViewModel = viewModel()
    val valueList = remember { values.split(",") }
    val products by viewModel.products.collectAsState()

    val listState = rememberLazyGridState()

    // 처음 한 번 API 호출
    LaunchedEffect(category, type, values) {
        viewModel.fetchRecommendations(
            category = category,
            type = type,
            values = valueList
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val typeLabel = when (type.lowercase()) {
                "color" -> "색상"
                "style" -> "스타일"
                else -> type  // 예외 처리를 위해 기본값
            }

            // 헤더 표시
            Text(
                text = "추천 결과 (${typeLabel} 기준)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(20.dp)
            )

            ProductGrid(
                products = products,
                navController = navController,
                listState = listState
            )
        }

        // 비교하기 플로팅 버튼
        CompareFloatingButton(
            onClick = {
                navController.navigate("likedList")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        )
    }
}

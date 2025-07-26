package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.ui.composable.BottomBar
import com.example.zippick.ui.composable.sizeSearch.ProductSorterForSize
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.model.SizeSearchResultHolder
import com.example.zippick.ui.model.SortOption
import com.example.zippick.ui.theme.MainBlue

@Composable
fun SizeSearchResultScreen(navController: NavHostController) {
    val selectedCategory = "책상" // 혹은 전달받는 값으로 처리 가능
    var selectedSort by remember { mutableStateOf(SortOption.NEWEST) }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = MainBlue, fontWeight = FontWeight.Bold)) {
                        append("\"$selectedCategory\"")
                    }
                    append("카테고리에 대한 사이즈 기반 검색 결과입니다.")
                },
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )

            ProductSorterForSize(
                productCount = SizeSearchResultHolder.totalCount,
                selectedSort = selectedSort,
                onSortChange = { selectedSort = it }
            )

            ProductGrid(products = SizeSearchResultHolder.products, navController = navController)
        }
    }
}

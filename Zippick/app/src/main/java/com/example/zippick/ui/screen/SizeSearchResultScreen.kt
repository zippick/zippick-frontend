package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.ui.composable.category.ProductFilterHeader
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.viewmodel.ProductViewModel

@Composable
fun SizeSearchResultScreen(
    navController: NavHostController,
    viewModel: ProductViewModel
) {
    val products by viewModel.products.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val selectedCategory = selectedCategoryGlobal
    val selectedSort by viewModel.sizeSearchSortOption.collectAsState()

    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@collect
                val totalItems = layoutInfo.totalItemsCount
                if (lastVisibleItem >= totalItems - 4) {
                    viewModel.loadMore()
                }
            }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MainBlue, fontWeight = FontWeight.Bold)) {
                        append("\"$selectedCategory\" ")
                    }
                    append("카테고리에 대한 사이즈 기반 검색 결과입니다.")
                },
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )

            ProductFilterHeader(
                productCount = totalCount,
                selectedSort = selectedSort,
                onSortChange = {
                    viewModel.setSizeSearchSortOption(it)
                    viewModel.reloadWithSort(it.code)
                },
                minPrice = null,
                maxPrice = null,
                onMinPriceChange = null,
                onMaxPriceChange = null,
                onPriceFilterApply = null
            )

            when {
                error != null -> Text("에러: $error", modifier = Modifier.padding(16.dp))
                products.isEmpty() && !loading -> Text("결과가 없습니다.", modifier = Modifier.padding(16.dp))
                else -> ProductGrid(
                    products = products,
                    navController = navController,
                    listState = listState,
                    isLoading = loading,
                    onLoadMore = {
                        viewModel.loadMore()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

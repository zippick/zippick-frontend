package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.ui.composable.sizeSearch.ProductSorterForSize
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.snapshotFlow

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
    var selectedSort by rememberSaveable { mutableStateOf(viewModel.selectedSortOption) }

    val listState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { layoutInfo ->
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@collectLatest
                if (lastVisible >= products.size - 4 && !loading && products.size < totalCount) {
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

            ProductSorterForSize(
                productCount = totalCount,
                selectedSort = selectedSort,
                onSortChange = {
                    selectedSort = it
                    viewModel.setSortOption(it)
                    viewModel.reloadWithSort(it.code)
                }
            )

            when {
                error != null -> Text("에러: $error", modifier = Modifier.padding(16.dp))
                products.isEmpty() && !loading -> Text("결과가 없습니다.", modifier = Modifier.padding(16.dp))
                else -> ProductGrid(
                    products = products,
                    navController = navController,
                    listState = listState,
                    isLoading = loading // 로딩 인디케이터 표시를 위한 전달
                )
            }
        }
    }
}

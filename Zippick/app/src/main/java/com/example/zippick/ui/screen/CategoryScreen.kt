package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zippick.ui.composable.category.CategoryFilterBar
import com.example.zippick.ui.composable.category.ProductFilterHeader
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.model.SortOption
import com.example.zippick.ui.viewmodel.ProductViewModel
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.zippick.ui.composable.category.CompareFloatingButton
import com.example.zippick.ui.theme.MainBlue

@Composable
fun CategoryScreen(
    navController: NavController,
    keyword: String? = null // 검색어가 있으면 검색 모드
) {
    val isSearchMode = keyword != null

    var selectedCategory by remember { mutableStateOf("전체") }
    val categories = listOf("전체", "의자", "소파", "책상", "식탁", "옷장", "침대")

    var selectedSort by remember { mutableStateOf(SortOption.NEWEST) }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    val productViewModel: ProductViewModel = viewModel()
//    val products = sampleProducts
    // TODO: 백엔드 API 수정되면 아래로 교체
    val products by productViewModel.products.collectAsState()
    val totalCount by productViewModel.totalCount.collectAsState()
    val isLoading by productViewModel.loading.collectAsState()

    val listState = rememberLazyGridState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalCount = listState.layoutInfo.totalItemsCount
            lastVisible >= totalCount - 2
        }
    }

    // 검색어 또는 정렬이 바뀔 때 API 호출
    LaunchedEffect(keyword, selectedSort) {
        println("🔍 검색 모드 API 호출됨: $keyword / 정렬: $selectedSort")
        if (isSearchMode) {
            productViewModel.searchProductsByKeyword(
                keyword = keyword ?: "",
                sort = selectedSort.code,
                offset = 0,
                append = false
            )
        } else {
            productViewModel.loadByCategoryAndPrice(
                category = selectedCategory,
                minPrice = minPrice,
                maxPrice = maxPrice,
                sort = selectedSort.code,
                offset = 0,
                append = false
            )
        }
    }

    // 카테고리, 가격, 정렬 변경 시 API 호출
    LaunchedEffect(selectedCategory, minPrice, maxPrice, selectedSort) {
        println("📦 카테고리 모드 API 호출됨: $selectedCategory / $minPrice~$maxPrice")
        if (!isSearchMode) {
            productViewModel.loadByCategoryAndPrice(
                category = selectedCategory,
                minPrice = minPrice,
                maxPrice = maxPrice,
                sort = selectedSort.code,
                offset = 0,
                append = false
            )
        }
    }

    // 무한스크롤 감지 시 추가 로딩
    LaunchedEffect(shouldLoadMore.value) {
        if (isSearchMode && shouldLoadMore.value && !isLoading) {
            productViewModel.loadMoreProducts()
        } else if (!isSearchMode && shouldLoadMore.value && !isLoading) {
            productViewModel.loadMoreByCategoryAndPrice()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            if (!isSearchMode) {
                CategoryFilterBar(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MainBlue,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("‘$keyword’")
                        }
                        append("에 대한 검색 결과입니다.")
                    },
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            ProductFilterHeader(
                productCount = totalCount,
                selectedSort = selectedSort,
                onSortChange = { selectedSort = it },
                minPrice = if (!isSearchMode) minPrice else null,
                maxPrice = if (!isSearchMode) maxPrice else null,
                onMinPriceChange = if (!isSearchMode) ({ minPrice = it }) else null,
                onMaxPriceChange = if (!isSearchMode) ({ maxPrice = it }) else null,
                onPriceFilterApply = if (!isSearchMode) {
                    {
                        productViewModel.loadByCategoryAndPrice(
                            category = selectedCategory,
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            sort = selectedSort.code,
                            offset = 0,
                            append = false
                        )
                    }
                } else null
            )

            ProductGrid(
                products = products,
                navController = navController,
                listState = listState
            )
        }

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

package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    productViewModel: ProductViewModel = viewModel(),
    keyword: String? = null,
    initialCategory: String = "전체"
) {
    val isSearchMode = keyword != null

    var selectedCategory by remember { mutableStateOf(initialCategory) }
    val categories = listOf("전체", "의자", "소파", "책상", "식탁", "옷장", "침대")

    val sortInitKey = rememberSaveable(key = keyword) { mutableStateOf(true) } // 최초 진입 판단 키
    val selectedSort by productViewModel.categorySortOption.collectAsState()

    var minPrice by rememberSaveable { mutableStateOf("0") }
    var maxPrice by rememberSaveable { mutableStateOf("") }

    val products by productViewModel.products.collectAsState()
    val totalCount by productViewModel.totalCount.collectAsState()
    val isLoading by productViewModel.loading.collectAsState()

    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@collect
                val totalItems = layoutInfo.totalItemsCount
                if (lastVisibleItem >= totalItems - 4) {
                    if (isSearchMode) {
                        productViewModel.loadMoreProducts()
                    } else {
                        productViewModel.loadMoreByCategoryAndPrice()
                    }
                }
            }
    }

    // 키워드가 바뀌면 최초 1회만 정렬 초기화
    LaunchedEffect(keyword) {
        if (isSearchMode && sortInitKey.value) {
            productViewModel.setCategorySortOption(SortOption.LATEST)
            sortInitKey.value = false
        }
    }

    // 검색어 또는 정렬이 바뀔 때 API 호출
    LaunchedEffect(keyword, selectedSort) {
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
                onSortChange = {
                    productViewModel.setCategorySortOption(it)
                },
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
                listState = listState,
                isLoading = isLoading,
                onLoadMore = {
                    if (isSearchMode) {
                        productViewModel.loadMoreProducts()
                    } else {
                        productViewModel.loadMoreByCategoryAndPrice()
                    }
                },
                modifier = Modifier.weight(1f)
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

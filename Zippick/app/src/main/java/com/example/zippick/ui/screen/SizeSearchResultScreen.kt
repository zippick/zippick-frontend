package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.zippick.ui.composable.category.CategoryFilterBar
import com.example.zippick.ui.composable.category.ProductFilterHeader
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.model.SizeSearchResultHolder
import com.example.zippick.ui.model.SortOption

@Composable
fun SizeSearchResultScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("전체") }
    val categories = listOf("전체", "의자", "소파", "책상", "식탁", "옷장", "침대")

    var selectedSort by remember { mutableStateOf(SortOption.NEWEST) }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    Column {
        CategoryFilterBar(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        ProductFilterHeader(
            productCount = SizeSearchResultHolder.totalCount,
            selectedSort = selectedSort,
            onSortChange = { selectedSort = it },
            minPrice = minPrice,
            maxPrice = maxPrice,
            onMinPriceChange = { minPrice = it },
            onMaxPriceChange = { maxPrice = it }
        )

        ProductGrid(products = SizeSearchResultHolder.products, navController = navController)
    }
}

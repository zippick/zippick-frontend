package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.zippick.ui.composable.category.CategoryFilterBar
import com.example.zippick.ui.composable.category.ProductFilterHeader
import com.example.zippick.ui.composable.category.ProductGrid
import com.example.zippick.ui.model.dummy.sampleProducts

import com.example.zippick.ui.model.SortOption
@Composable
fun CategoryScreen(
    navController: NavController
) {
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
            productCount = 20,
            selectedSort = selectedSort,
            onSortChange = { selectedSort = it },
            minPrice = minPrice,
            maxPrice = maxPrice,
            onMinPriceChange = { minPrice = it },
            onMaxPriceChange = { maxPrice = it }
        )
        ProductGrid(products = sampleProducts)
    }
}
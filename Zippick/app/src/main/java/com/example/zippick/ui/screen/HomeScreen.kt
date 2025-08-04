package com.example.zippick.ui.screen

import CarouselSection
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.ui.composable.home.CategoryGrid

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        CarouselSection()
        Spacer(modifier = Modifier.height(20.dp))
        CategoryGrid(navController = navController)
    }
}


package com.example.zippick.ui.screen

import CarouselSection
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        CarouselSection()
        Spacer(modifier = Modifier.height(16.dp))
        // 이후 다른 콘텐츠...
    }
}


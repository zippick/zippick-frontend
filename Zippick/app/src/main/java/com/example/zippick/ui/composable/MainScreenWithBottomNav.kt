package com.example.zippick.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import com.example.zippick.ui.screen.CategoryScreen
import com.example.zippick.ui.screen.HomeScreen
import com.example.zippick.ui.screen.MyScreen
import com.example.zippick.ui.screen.PhotoScreen
import com.example.zippick.ui.screen.SizeScreen

@Composable
fun MainScreenWithBottomNav() {
    var selectedRoute by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedRoute = selectedRoute,
                onItemSelected = { selectedRoute = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedRoute) {
                "home" -> HomeScreen()
                "category" -> CategoryScreen()
                "size" -> SizeScreen()
                "photo" -> PhotoScreen()
                "my" -> MyScreen()
            }
        }
    }
}

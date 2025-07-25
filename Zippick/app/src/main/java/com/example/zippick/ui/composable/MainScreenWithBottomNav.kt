package com.example.zippick.ui.composable

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zippick.ui.screen.AiLayoutScreen
import com.example.zippick.ui.screen.CategoryScreen
import com.example.zippick.ui.screen.CategorySelectionScreen
import com.example.zippick.ui.screen.DetailScreen
import com.example.zippick.ui.screen.HomeScreen
import com.example.zippick.ui.screen.MyScreen
import com.example.zippick.ui.screen.NotificationScreen
import com.example.zippick.ui.screen.PhotoScreen
import com.example.zippick.ui.screen.SizeSearchResultScreen
import com.example.zippick.ui.screen.SearchScreen
import com.example.zippick.ui.screen.SizeInputScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zippick.ui.viewmodel.ProductViewModel
import androidx.core.net.toUri
import com.example.zippick.ui.screen.PhotoAnalysisResultScreen

@Composable
fun MainScreenWithBottomNav(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val bottomTabs = listOf("home", "category", "size", "photo", "my")
    val productViewModel: ProductViewModel = viewModel()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                TopBar(navController = navController, currentRoute = currentRoute)
            }
        },
        bottomBar = {
            if (bottomTabs.any { currentRoute.startsWith(it) }) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.White) // 원하는 색상 적용
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen(navController) }
                composable("category") { CategoryScreen(navController) }
                composable("size") { CategorySelectionScreen(navController) }
                composable("photo") { PhotoScreen(navController) }
                composable("my") { MyScreen(navController) }

                // 검색/알림/상세 페이지 등 추가
                composable("search") { SearchScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("detail/{itemId}") { backStackEntry ->
                    DetailScreen(
                        navController = navController,
                        itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                    )
                }

                // ai 가구 배치
                composable("aiLayout") { AiLayoutScreen(navController) }

                // 사이즈 기반 검색
                composable("sizeInput/{category}") { backStackEntry ->
                    val category = backStackEntry.arguments?.getString("category") ?: "의자"
                    SizeInputScreen(navController, category, productViewModel)
                }
                composable("sizeSearchResult") {
                    SizeSearchResultScreen(navController, productViewModel)
                }

                // 사진 기반 검색 결과
                composable(
                    route = "photoAnalysis/{imageUri}",
                    arguments = listOf(navArgument("imageUri") { defaultValue = "" })
                ) { backStackEntry ->
                    val uriStr = backStackEntry.arguments?.getString("imageUri") ?: ""
                    val decodedUri = Uri.decode(uriStr).toUri()

                    PhotoAnalysisResultScreen(navController, decodedUri)
                }
            }
        }
    }
}

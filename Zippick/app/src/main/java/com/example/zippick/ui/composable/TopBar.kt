package com.example.zippick.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    currentRoute: String
) {
    val bottomTabs = listOf("home", "category", "size", "photo", "my")
    val isBottomTab = currentRoute in bottomTabs
    val isLoginScreen = currentRoute == "login"
    val isSignupScreen = currentRoute == "signup"

    TopAppBar(
        title = {
            if (!isLoginScreen && !isSignupScreen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when {
                            currentRoute == "home" -> "홈"
                            currentRoute == "category" -> "카테고리"
                            currentRoute == "size" -> "사이즈 검색"
                            currentRoute == "photo" -> "AI 추천"
                            currentRoute == "my" -> "마이페이지"
                            currentRoute == "search" -> "검색"
                            currentRoute == "notifications" -> "알림함"
                            currentRoute == "aiLayout" -> "AI 가구 배치"
                            currentRoute.startsWith("sizeInput") -> "사이즈 검색"
                            currentRoute.startsWith("category/") -> "상품 목록"
                            currentRoute.startsWith("searchResult") -> "상품 목록"
                            currentRoute == "sizeSearchResult" -> "상품 목록"
                            currentRoute.startsWith("photoAnalysis") -> "분석 결과"
                            currentRoute == "likedList" -> "찜 목록"
                            currentRoute.startsWith("categoryCompareResult") -> "상품 비교"
                            currentRoute.startsWith("photoRecommendList") -> "추천 상품"
                            currentRoute.startsWith("myOrderDetail") -> "주문 상세"
                            currentRoute.startsWith("categoryAiLayout") -> "가상 배치"
                            else -> "상세페이지"
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                }
            }
        },
        navigationIcon = {
            when {
                isBottomTab -> {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(50.dp)
                            .clickable { navController.navigate("home") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_new_logo),
                            contentDescription = "로고",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                isSignupScreen -> {
                    IconButton(onClick = {
                        navController.popBackStack("home", inclusive = false)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "닫기",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                isLoginScreen -> {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                else -> {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        actions = {
            if (!isLoginScreen && !isSignupScreen) {
                IconButton(onClick = { navController.navigate("search") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "검색",
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { navController.navigate("notifications") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alarm),
                        contentDescription = "알림함",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

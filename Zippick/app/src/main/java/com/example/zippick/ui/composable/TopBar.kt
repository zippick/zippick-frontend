package com.example.zippick.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    currentRoute: String
) {
    val bottomTabs = listOf("home", "category", "size", "photo", "my")
    val isBottomTab = currentRoute in bottomTabs

    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp), // 오른쪽 액션 아이콘 공간만큼 패딩
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        currentRoute == "home" -> "홈"
                        currentRoute == "category" -> "카테고리"
                        currentRoute == "size" -> "사이즈 검색"
                        currentRoute == "photo" -> "사진"
                        currentRoute == "my" -> "마이페이지"
                        currentRoute == "search" -> "검색"
                        currentRoute == "notifications" -> "알림함"
                        currentRoute == "aiLayout" -> "AI 가구 배치"
                        currentRoute.startsWith("sizeInput") -> "사이즈 검색"
                        currentRoute == "sizeSearchResult" -> "검색 결과"
                        else -> "상세페이지"
                    },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        },
        navigationIcon = {
            if (isBottomTab) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "로고",
                    modifier = Modifier.padding(start = 16.dp).size(32.dp)
                )
            } else {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        actions = {
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}


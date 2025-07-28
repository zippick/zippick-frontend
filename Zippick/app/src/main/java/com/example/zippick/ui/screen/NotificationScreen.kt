package com.example.zippick.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.network.alert.ApiService

data class NotificationItem(
    val title: String,
    val time: String,
    val desc: String
)

@Composable
fun NotificationScreen(
    navController: NavController,
    apiService: ApiService
) {
    var notifications by remember { mutableStateOf<List<NotificationItem>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // TopBar에서 버튼 클릭 시 isLoaded = false로 초기화하고 화면 이동 또는 true로 변경
    LaunchedEffect(isLoaded) {
        if (!isLoaded) {
            try {
                notifications = apiService.getNotifications() // 서버에서 알림 가져옴
            } catch (e: Exception) {
                Toast.makeText(context, "알림을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
            isLoaded = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        items(notifications) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = Color(0xFFEAEAEA),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 체크 아이콘(실제 Icon 사용 시 바꿔도 됨)
                        Text(
                            text = "✓",
                            fontSize = 20.sp,
                            color = Color(0xFF222222),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = " · ${item.time}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.desc,
                        fontSize = 15.sp,
                        color = Color(0xFF222222)
                    )
                }
            }
        }
    }
}

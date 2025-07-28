package com.example.zippick.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.ui.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current

    // 화면 진입시 서버에서 데이터 요청
    LaunchedEffect(Unit) {
        viewModel.loadNotifications(offset = 0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        if (loading) {
            Text(
                "로딩 중...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
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
                                text = " · ${formatToKST(item.createdAt)}", // 시간 필드에 맞게 수정
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.body, // desc 대신 body 필드로 맞춤
                            fontSize = 15.sp,
                            color = Color(0xFF222222)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatToKST(input: String?): String {
    if (input.isNullOrBlank()) return ""
    return try {
        // 1. OffsetDateTime(ISO 8601 with Z or +09:00) 파싱 시도
        val zdt = java.time.OffsetDateTime.parse(input)
        val kst = zdt.atZoneSameInstant(java.time.ZoneId.of("Asia/Seoul"))
        java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(kst)
    } catch (e: Exception) {
        try {
            // 2. LocalDateTime(yyyy-MM-ddTHH:mm:ss)로 시도, KST로 변환
            val ldt = java.time.LocalDateTime.parse(input)
            val kst = ldt.atZone(java.time.ZoneId.of("Asia/Seoul"))
            java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(kst)
        } catch (e: Exception) {
            ""
        }
    }
}
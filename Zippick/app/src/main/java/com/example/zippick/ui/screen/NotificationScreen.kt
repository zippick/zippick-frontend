package com.example.zippick.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class NotificationItem(
    val title: String,
    val time: String,
    val desc: String
)

@Composable
fun NotificationScreen(
    navController: NavController
) {
    val notifications = listOf(
        NotificationItem(
            title = "결제 완료",
            time = "2025.07.10",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.06.1",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.05.17",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.07.10",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.06.1",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.05.17",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.07.10",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.06.1",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.05.17",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.07.10",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.06.1",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.05.17",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.07.10",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.06.1",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        ),
        NotificationItem(
            title = "결제 완료",
            time = "2025.05.17",
            desc = "리바트 뉴 테크닉 의자 결제가 완료되었습니다."
        )
    )

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

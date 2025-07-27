package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun MyScreen(navController: NavHostController) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var orders by remember { mutableStateOf(generateInitialOrders()) }
    var isLoading by remember { mutableStateOf(false) }

    // Load more when reaching the bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .map { it == orders.lastIndex }
            .distinctUntilChanged()
            .filter { it && !isLoading }
            .collectLatest {
                isLoading = true
                delay(1000) // simulate loading delay
                orders = orders + generateMoreOrders(orders.size)
                isLoading = false
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            state = listState,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MainBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    ) {
                        Text("김현지", style = Typography.titleLarge, color = Color.White)
                        Text("hyunzl@naver.com", style = Typography.bodyLarge, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "로그아웃",
                            color = Color.White,
                            style = Typography.bodyLarge.copy(fontSize = 14.sp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    // TODO: 로그아웃 로직 연결
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "주문 내역",
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }

            items(orders) { order ->
                OrderItem(order.date, order.number, order.status)
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("로딩 중...", style = Typography.bodyLarge)
                    }
                }
            }
        }
    }
}

// 주문 데이터 클래스
data class Order(val date: String, val number: String, val status: String)

// 초기 주문 리스트
fun generateInitialOrders(): List<Order> = List(10) {
    Order("25.07.22", "2025072201${it.toString().padStart(2, '0')}", "결제 완료")
}

// 추가 주문 리스트
fun generateMoreOrders(startIndex: Int): List<Order> = List(5) {
    Order("25.07.22", "2025072201${(startIndex + it).toString().padStart(2, '0')}", "배송 준비중")
}

@Composable
fun OrderItem(date: String, number: String, status: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(date, style = Typography.bodyLarge, color = Color.Black)
                Text("주문 번호 $number", style = Typography.bodyLarge.copy(fontSize = 13.sp))
            }
            Text(status, style = Typography.bodyLarge.copy(color = MainBlue))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.chair),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("리바트 뉴 테크닉 의자", style = Typography.bodyLarge)
                Text(
                    "(싱글헤드형 - 블랙쉘 다크그레이)",
                    style = Typography.bodyLarge.copy(fontSize = 13.sp, color = Color.Gray)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

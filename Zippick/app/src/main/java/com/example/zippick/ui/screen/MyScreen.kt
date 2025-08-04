package com.example.zippick.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.TokenManager
import com.example.zippick.network.auth.AuthService
import com.example.zippick.network.member.MemberService
import com.example.zippick.ui.composable.RequireLogin
import com.example.zippick.ui.model.MyInfoResponse
import com.example.zippick.ui.model.OrderHistoryResponse
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.MediumGray
import com.example.zippick.ui.theme.Typography
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun MyScreen(navController: NavHostController) {

    RequireLogin(navController = navController) {

        val listState = rememberLazyListState()
        var myInfo by remember { mutableStateOf<MyInfoResponse?>(null) }
        var orders by remember { mutableStateOf<List<OrderHistoryResponse>>(emptyList()) }
        var isLoading by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        val authService = remember { RetrofitInstance.retrofit.create(AuthService::class.java) }
        val memberService = remember {
            RetrofitInstance.retrofit.create(MemberService::class.java)
        }

        LaunchedEffect(Unit) {
            try {
                isLoading = true
                val token = "Bearer ${TokenManager.getToken()}"

                myInfo = memberService.getMyInfo(token)

                orders = memberService.getOrderHistories(token)
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    TokenManager.clearToken()
                    navController.navigate("login") {
                        popUpTo("my") { inclusive = true }
                    }
                }
            } finally {
                isLoading = false
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    myInfo?.let { info ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MainBlue)
                                .padding(24.dp), // 외부 여백
                            color = MainBlue
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = info.name,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = info.loginId,
                                            style = Typography.bodyMedium,
                                            color = Color.DarkGray
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            coroutineScope.launch {
                                                val token = TokenManager.getToken() ?: return@launch
                                                try {
                                                    val response = authService.logout(token)
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                } finally {
                                                    TokenManager.clearToken()
                                                    navController.navigate("login") {
                                                        popUpTo("my") { inclusive = true }
                                                    }
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ExitToApp,
                                            contentDescription = "로그아웃",
                                            tint = MainBlue
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "로그아웃",
                                            color = MainBlue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(24.dp))
                        Column {
                            Text(
                                "주문 내역",
                                modifier = Modifier
                                    .padding(horizontal = 26.dp)
                                    .padding(top = 12.dp),
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                fontSize = 20.sp
                            )

                            Divider(
                                color = MediumGray,
                                thickness = 1.5.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 26.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
                if (!isLoading && orders.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                                .padding(horizontal = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "주문 내역이 없습니다",
                                color = Color.Gray,
                                style = Typography.bodyLarge.copy(fontSize = 14.sp)
                            )
                        }
                    }
                }
                items(orders) { order ->
                    OrderItem(
                        order = order,
                        modifier = Modifier.padding(horizontal = 26.dp)
                    ) { orderId ->
                        navController.navigate("myOrderDetail/${order.id}")
                    }
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
}



@Composable
fun OrderItem(
    order: OrderHistoryResponse,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit) {

    val statusText = when (order.status) {
        "ORDERED" -> "결제 완료"
        "CANCELED" -> "결제 취소"
        else -> "알 수 없음"
    }

    val statusColor = when (order.status) {
        "ORDERED" -> MainBlue
        "CANCELED" -> MediumGray
        else -> Color.Gray
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(order.id) }
            .padding(top = 12.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(order.createdAt.substring(0, 10).toShortDateFormat(), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight(500))
                Spacer(modifier = Modifier.height(10.dp))
                Text("주문번호 ${order.merchantOrderId}", style = Typography.bodyLarge.copy(fontSize = 15.sp))
            }
            Text(statusText, style = Typography.bodyLarge.copy(color = statusColor), fontWeight = FontWeight(500))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = order.productImageUrl,
                contentDescription = null,
                modifier = Modifier.size(84.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(order.productName, fontWeight = FontWeight(500))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

fun String.toShortDateFormat(): String {
    return try {
        val parts = this.split(" ")[0].split("-")
        val year = parts[0].takeLast(2)
        "$year.${parts[1]}.${parts[2]}"
    } catch (e: Exception) {
        this // 실패 시 원본 반환
    }
}



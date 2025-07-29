package com.example.zippick.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.zippick.ui.theme.Typography
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun MyScreen(navController: NavHostController) {
    Log.d("ZIPPICK", "🧭 MyScreen: Composable loaded")

    RequireLogin(navController = navController) {
        Log.d("ZIPPICK", "🔓 MyScreen: RequireLogin 통과")

        val listState = rememberLazyListState()
        var myInfo by remember { mutableStateOf<MyInfoResponse?>(null) }
        var orders by remember { mutableStateOf<List<OrderHistoryResponse>>(emptyList()) }
        var isLoading by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        val authService = remember { RetrofitInstance.retrofit.create(AuthService::class.java) }
        val memberService = remember {
            Log.d("ZIPPICK", "📦 MyScreen: MemberService 초기화")
            RetrofitInstance.retrofit.create(MemberService::class.java)
        }

        LaunchedEffect(Unit) {
            try {
                isLoading = true
                val token = "Bearer ${TokenManager.getToken()}"
                Log.d("ZIPPICK", "📡 MyScreen: myInfo, orderHistories 요청 시작 / token=$token")

                myInfo = memberService.getMyInfo(token)
                Log.d("ZIPPICK", "✅ MyScreen: myInfo.name = ${myInfo?.name}")

                orders = memberService.getOrderHistories(token)
                Log.d("ZIPPICK", "✅ MyScreen: 주문 개수 = ${orders.size}")
            } catch (e: Exception) {
                Log.e("ZIPPICK", "❌ MyScreen: 예외 발생", e)
                if (e is HttpException && e.code() == 401) {
                    Log.w("ZIPPICK", "🔐 MyScreen: 토큰 만료 → 로그인 화면 이동")
                    TokenManager.clearToken()
                    navController.navigate("login") {
                        popUpTo("my") { inclusive = true }
                    }
                }
            } finally {
                Log.d("ZIPPICK", "🕓 MyScreen: isLoading = false")
                isLoading = false
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    myInfo?.let { info ->
                        Log.d("ZIPPICK", "🧾 MyScreen: 사용자 정보 UI 렌더링 / ${info.name}")

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MainBlue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(info.name, style = Typography.titleLarge, color = Color.White)
                                Text(info.loginId, style = Typography.bodyLarge, color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "로그아웃",
                                    color = Color.White,
                                    style = Typography.bodyLarge.copy(fontSize = 14.sp),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .clickable {
                                            Log.d("ZIPPICK", "🚪 MyScreen: 로그아웃 클릭됨")

                                            coroutineScope.launch {
                                                val token = TokenManager.getToken() ?: return@launch
                                                try {
                                                    val response = authService.logout(token) // Bearer 없이 전달
                                                    Log.d("ZIPPICK", "🚪 로그아웃 응답 성공 = ${response.isSuccessful}")
                                                } catch (e: Exception) {
                                                    Log.e("ZIPPICK", "❌ 로그아웃 요청 실패", e)
                                                } finally {
                                                    TokenManager.clearToken()
                                                    navController.navigate("login") {
                                                        popUpTo("my") { inclusive = true }
                                                    }
                                                }
                                            }
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
                }

                items(orders) { order ->
                    Log.d("ZIPPICK", "📦 MyScreen: 주문 항목 렌더링 / ${order.productName}")
                    OrderItem(order)
                }

                if (isLoading) {
                    item {
                        Log.d("ZIPPICK", "⏳ MyScreen: 로딩 중 표시")
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
fun OrderItem(order: OrderHistoryResponse) {
    Log.d("ZIPPICK", "🧾 OrderItem: 렌더링 시작 - ${order.productName}")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(order.createdAt.substring(0, 10), style = Typography.bodyLarge, color = Color.Black)
                Text("주문 번호 ${order.merchantOrderId}", style = Typography.bodyLarge.copy(fontSize = 13.sp))
            }
            Text(order.status, style = Typography.bodyLarge.copy(color = MainBlue))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = order.productImageUrl,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(order.productName, style = Typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}


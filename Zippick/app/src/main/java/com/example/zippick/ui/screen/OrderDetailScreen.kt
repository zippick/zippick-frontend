package com.example.zippick.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.network.TokenManager
import com.example.zippick.network.order.OrderRepository
import com.example.zippick.ui.composable.orderDetail.OrderDetailContent
import com.example.zippick.ui.composable.photo.LottieLoading
import com.example.zippick.ui.model.OrderDetailResponse
import kotlinx.coroutines.launch

@Composable
fun OrderDetailScreen(
    orderId: Int,
    navController: NavController
) {
    val repository = remember { OrderRepository() }
    val coroutineScope = rememberCoroutineScope()
    var orderDetail by remember { mutableStateOf<OrderDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        try {
            val token = TokenManager.getToken()
            val response = repository.getOrderDetail(orderId, "Bearer $token")

            if (response.isSuccessful) {
                orderDetail = response.body()
            } else {
                Toast.makeText(context, "주문 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "에러 발생: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }


    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieLoading(modifier = Modifier.size(90.dp))
            }
        }
    } else {
        orderDetail?.let {
            OrderDetailContent(
                orderDetail = it,
                onCancelClick = {
                    coroutineScope.launch {
                        try {
                            val token = TokenManager.getToken()
                            val response = repository.cancelOrder(it.orderId, "Bearer $token")
                            Log.e("CancelOrder", "HTTP ${response.code()} - ${response.errorBody()?.string()}")

                            if (response.isSuccessful) {
                                Toast.makeText(context, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // 이전 화면으로 돌아가기
                            } else {
                                Toast.makeText(context, "주문 취소에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("OrderCancel", "에러: ${e.message}", e)
                            Toast.makeText(context, "에러 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                navController = navController
            )
        }

    }
}

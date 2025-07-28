package com.example.zippick.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.zippick.ui.composable.orderDetail.OrderDetailContent
import com.example.zippick.ui.model.OrderDetailResponse

@Composable
fun OrderDetailScreen(
    orderId: Int,
    navController: NavController
) {
    // 실제로는 viewModel 등에서 받아올 수 있도록 처리
    val orderDetail = OrderDetailResponse(
        orderDate = "2025.07.22",
        orderNumber = "202507220123",
        name = "김현지",
        address = "서울 영등포구 여찌구동여찌구 201호",
        productId = 1,
        productName = "리바트 뉴 테크닉 의자 (싱글헤드형-블랙쉘 다크그레이)",
        thumbnailImageUrl = "https://cdn.011st.com/11dims/resize/1000x1000/quality/75/11src/product/1854552772/B.jpg?445000000",
        count = 1,
        price = 150000,
        totalPrice = 150000
    )

    OrderDetailContent(orderDetail = orderDetail, onCancelClick = {
        // 주문 취소 처리 로직
    })
}

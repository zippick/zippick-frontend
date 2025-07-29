package com.example.zippick.ui.composable.orderDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.zippick.ui.model.OrderDetailResponse
import com.example.zippick.ui.theme.MainBlue

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetailResponse,
    onCancelClick: () -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(24.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = orderDetail.createdAt.toShortDateFormat(),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "주문번호 ${orderDetail.merchantOrderId}")

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = orderDetail.memberName,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = orderDetail.basicAddress + " " + orderDetail.detailAddress)

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "주문 상품",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("detail/${orderDetail.productId}")
                }
        ) {
            AsyncImage(
                model = orderDetail.productImageUrl,
                contentDescription = "상품 이미지",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = orderDetail.productName, fontSize = 16.sp,fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${orderDetail.count}개")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${orderDetail.productPrice.toPriceFormat()}원")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "최종 결제 금액",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${orderDetail.totalPrice.toPriceFormat()}원",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 주문 상태가 취소가 아닌 경우에만 버튼 보여주기
        if (orderDetail.status != "CANCELED") {
            Button(
                onClick = { showDialog = true }, // 팝업 띄우기
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.8.dp, MainBlue),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainBlue,
                    contentColor = Color.White
                )
            ) {
                Text(text = "주문 취소", fontWeight = FontWeight(500), fontSize = 16.sp)
            }
        }

        if (showDialog && orderDetail.status != "CANCELED") {
            CancelOrderDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    onCancelClick()
                }
            )
        }
    }
}

fun Int.toPriceFormat(): String {
    return "%,d".format(this)
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


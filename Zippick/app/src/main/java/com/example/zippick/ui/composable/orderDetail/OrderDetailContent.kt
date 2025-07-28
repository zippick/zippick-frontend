package com.example.zippick.ui.composable.orderDetail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zippick.ui.model.OrderDetailResponse
import com.example.zippick.ui.theme.MainBlue

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetailResponse,
    onCancelClick: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = orderDetail.orderDate,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "주문 번호 ${orderDetail.orderNumber}")

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = orderDetail.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = orderDetail.address)

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "주문 상품",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = orderDetail.thumbnailImageUrl,
                contentDescription = "상품 이미지",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = orderDetail.productName)
                Text(text = "${orderDetail.count}개")
                Text(text = "${orderDetail.price}원")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "최종 결제 금액", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "${orderDetail.totalPrice}원", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = BorderStroke(1.8.dp, MainBlue),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBlue,
                contentColor = Color.White
            )
        ) {
            Text(text = "주문 취소")
        }
    }
}

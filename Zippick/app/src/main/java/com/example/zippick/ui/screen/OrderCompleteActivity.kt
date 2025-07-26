package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.example.zippick.R


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class OrderCompleteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderCompleteScreen()
        }
    }
}

@Composable
fun OrderCompleteScreen() {
    val orderDate = "2025년 07월 21일" //주문일시
    val orderNumber = "202507220123" //주문번호
    val productName = "테스트상품명" //상품명
    val productPrice = 2000 //가격
    val productAmount = 1 //수량
    val productImage = R.drawable.chair // 상품 이미지

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp) // 중앙보다 약간 위에서 시작 (숫자 조정 가능)
        ) {
            // 체크 아이콘 (Circle + Check)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = Color(0xFF1574AF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "완료",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "주문이 완료되었습니다",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "주문 일시 : $orderDate",
                style = TextStyle(fontSize = 13.sp, color = Color.Gray)
            )
            Text(
                text = "주문 번호 : $orderNumber",
                style = TextStyle(fontSize = 13.sp, color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(36.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = productImage),
                    contentDescription = "상품 이미지",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(productName, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${productPrice}원 / ${productAmount}개", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "결제 완료",
                        color = Color(0xFF1574AF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { /* 결제 상세로 이동 등 */ }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
        }
    }
}
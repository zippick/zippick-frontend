package com.example.zippick.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.zippick.MainActivity

class OrderCompleteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val orderNumber = intent.getStringExtra("orderNumber")?:""
        val orderDate = intent.getStringExtra("orderDate")?: ""
        val productName = intent.getStringExtra("productName") ?: ""
        val productImage = intent.getStringExtra("productImage") ?: ""
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productAmount = intent.getIntExtra("productAmount", 0)
        val totalPrice = intent.getIntExtra("totalPrice", 0)

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: "orderComplete"
            Scaffold(
                containerColor = Color.White
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "orderComplete",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("orderComplete") {
                        OrderCompleteScreen(
                            orderNumber, orderDate, productName, productImage,
                            productPrice,  productAmount, totalPrice, navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCompleteScreen(
    orderNumber: String,
    orderDate: String,
    productName: String,
    productImage: String,
    productPrice: Int,
    productAmount: Int,
    totalPrice: Int,
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 200.dp)
    ) {
    // 체크 아이콘 (Circle + Check)
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color = Color(0xFF035A7E), shape = CircleShape),
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
            AsyncImage(
                model = productImage,
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF035A7E), // 진한 보라, 원하는 색상 지정
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 0.dp) // 내부 패딩은 0, 높이로 통일
        ) {
            Text(
                text = "홈으로 이동하기",
                color = Color.White, // 글씨 하얗게 명시
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
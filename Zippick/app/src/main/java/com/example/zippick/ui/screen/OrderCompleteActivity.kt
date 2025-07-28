package com.example.zippick.ui.screen

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import coil.compose.AsyncImage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.SnackbarHostState

import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zippick.ui.composable.BottomBar

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
            Scaffold(
                bottomBar = { BottomBar(navController) }
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
                    // BottomBar
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("category") {
                        CategoryScreen(navController)
                    }
                    composable("size") {
                        SizeScreen(navController)
                    }
                    composable("photo") {
                        PhotoScreen(navController)
                    }
                    composable("my") {
                        MyScreen(navController)
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
    navController: NavHostController, // BottomBar에서 쓰는 네비게이터 주입
    onHomeClick: () -> Unit = {}
) {
    // 결제 완료시 알럿 2초 후 자동으로 사라짐
    var showAlert by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        showAlert = true
        kotlinx.coroutines.delay(2000)
        showAlert = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = showAlert,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 40.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(vertical = 14.dp, horizontal = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "결제 완료! 주문이 정상 처리되었습니다.",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
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
        }
    }
}
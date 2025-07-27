package com.example.zippick.ui.composable.detail

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.R
import com.example.zippick.ui.model.ProductDetail
import com.example.zippick.ui.screen.PaymentMethodActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(product: ProductDetail, navController: NavController) {
    val productCode = remember { "P" + (10000000..99999999).random() }
    val context = LocalContext.current
    val bottomSheetState = remember { mutableStateOf(false) } //바텀시트
    var productAmount by remember { mutableStateOf(1) } // [추가] 수량 상태

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Button(
                onClick = {
                    bottomSheetState.value = true // 바텀시트 열기
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("구매하기", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // 메인 이미지
            Image(
                painter = painterResource(id = R.drawable.thumnail), // 예: R.drawable.chair
                contentDescription = "의자 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            // 제품 정보
            Column(modifier = Modifier.padding(30.dp)) {
                Text(
                    text = product.category,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "%,d원".format(product.price),
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))

                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(Modifier.height(16.dp))

                // 배송 정보
                DetailRow(title = "배송정보", value = "7일 이내")
                DetailRow(title = "상품코드", value = productCode)
                DetailRow(
                    title = "크기",
                    value = "${product.width} x ${product.depth} x ${product.height} (cm)"
                )
                DetailRow(title = "색상", value = product.color)
                DetailRow(title = "스타일", value = product.style)

                Spacer(modifier = Modifier.height(24.dp))

                AIVirtualPlacementButton {
                    navController.navigate("aiLayout")
                }
                // 상세 이미지
                Image(
                    painter = painterResource(id = R.drawable.product_detail),
                    contentDescription = "상세 이미지",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
        // 바텀시트
        if (bottomSheetState.value) {
            ModalBottomSheet(
                onDismissRequest = { bottomSheetState.value = false },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                // 수량 선택 및 결제하기 UI
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 상품 요약
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.thumnail),
                            contentDescription = "상품 썸네일",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(product.category, color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // 수량 조절
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { if (productAmount > 1) productAmount-- }) {
                            Text("-", fontSize = 24.sp)
                        }
                        Text("$productAmount", fontSize = 20.sp, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                        IconButton(onClick = { productAmount++ }) {
                            Text("+", fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // 가격
                    Text(
                        text = "%,d원".format(product.price * productAmount),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 결제하기 버튼
                    Button(
                        onClick = {
                            val intent = Intent(context, PaymentMethodActivity::class.java).apply {
                                putExtra("productId", product.id)
                                putExtra("productName", product.name)
                                putExtra("productImage", product.mainImageUrl)
                                putExtra("productPrice", product.price)
                                putExtra("productAmount", productAmount)
                            }
                            context.startActivity(intent)
                            bottomSheetState.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("%,d원 결제하기".format(product.price * productAmount), fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

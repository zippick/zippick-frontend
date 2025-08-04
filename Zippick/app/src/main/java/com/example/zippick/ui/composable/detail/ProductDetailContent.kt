package com.example.zippick.ui.composable.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.zippick.ui.model.ProductDetail
import com.example.zippick.ui.screen.PaymentMethodActivity
import com.example.zippick.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(product: ProductDetail, navController: NavController) {
    val productCode = remember { "P" + (10000000..99999999).random() }
    val context = LocalContext.current
    val bottomSheetState = remember { mutableStateOf(false) } //바텀시트
    var productAmount by remember { mutableStateOf(1) } // 수량 상태

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = {
                        handlePurchaseClick(navController) {
                            bottomSheetState.value = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
                ) {
                    Text("구매하기", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.White)
                }
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
            AsyncImage(
                model = product.mainImageUrl,
                contentDescription = "상품 이미지",
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
                Spacer(Modifier.height(10.dp))
                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "%,d원".format(product.price),
                    fontSize = 18.sp
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

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "내 방에 가구를 미리 배치해보세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )


                AIVirtualPlacementButton {
                    val encodedName = Uri.encode(product.name)
                    val encodedCategory = Uri.encode(product.category)
                    val encodedImageUrl = Uri.encode(product.mainImageUrl)

                    navController.navigate(
                        "categoryAiLayout/$encodedName/${product.price}/$encodedCategory/$encodedImageUrl"
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 상세 이미지
                AsyncImage(
                    model = product.detailImage,
                    contentDescription = "상세 이미지",
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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                tonalElevation = 8.dp,
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {

                    // 상품 카드
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = product.mainImageUrl,
                            contentDescription = "상품 썸네일",
                            modifier = Modifier
                                .size(62.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                product.category,
                                color = Color(0xFF757575),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // 가격 영역
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "%,d원".format(product.price),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF222222)
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                    Divider()
                    Spacer(Modifier.height(10.dp))

                    // 수량 조절
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("수량", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF5F5F5))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { if (productAmount > 1) productAmount-- },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Text("-", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    "$productAmount",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .padding(horizontal = 2.dp),
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = { productAmount++ },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    // 장바구니 담기 버튼
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
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "%,d원 결제하기".format(product.price * productAmount),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

fun handlePurchaseClick(
    navController: NavController,
    onLoggedIn: () -> Unit
) {
    val token = com.example.zippick.network.TokenManager.getToken()
    if (token.isNullOrBlank() || token == "null") {
        navController.navigate("login")
    } else {
        onLoggedIn()
    }
}


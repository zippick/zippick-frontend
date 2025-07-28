package com.example.zippick.ui.composable.detail

import android.net.Uri
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
import androidx.compose.ui.text.style.TextDecoration
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
                    val encodedName = Uri.encode(product.name)
                    val encodedCategory = Uri.encode(product.category)
                    val encodedImageUrl = Uri.encode(product.mainImageUrl)

                    navController.navigate(
                        "aiLayout/$encodedName/${product.price}/$encodedCategory/$encodedImageUrl"
                    )
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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    // 상단 바
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp, bottom = 16.dp)
                            .size(width = 36.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFE0E0E0))
                    )

                    // 상품 카드
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.thumnail),
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
//                            Spacer(Modifier.height(2.dp))
//                            Text(
//                                "아삭 아삭 씹는 재미", // 필요시 설명 수정
//                                color = Color(0xFFBDBDBD),
//                                fontSize = 12.sp
//                            )
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
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF222222)
                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            text = "25,900원", // 실제 할인 전 금액으로 변경 필요
//                            fontSize = 16.sp,
//                            color = Color(0xFFBDBDBD),
//                            textDecoration = TextDecoration.LineThrough
//                        )
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
                            containerColor = Color(0xFF7100D3) // 사진처럼 보라색 계열
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "%,d원 장바구니 담기".format(product.price * productAmount),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

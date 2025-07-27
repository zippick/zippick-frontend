package com.example.zippick.ui.composable.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.R
import com.example.zippick.ui.model.AiLayoutProduct
import com.example.zippick.ui.model.ProductDetail
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun ProductDetailContent(product: ProductDetail, navController: NavController) {
    val productCode = remember { "P" + (10000000..99999999).random() }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Button(
                onClick = { /* 구매하기 처리 */ },
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
                    val aiProduct = AiLayoutProduct(
                        name = product.name,
                        price = product.price,
                        category = product.category,
                        imageUrl = product.mainImageUrl
                    )
                    val json = Uri.encode(Json.encodeToString(aiProduct))
                    navController.navigate("aiLayout/$json")
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
    }
}

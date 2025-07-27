package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.ui.composable.compare.SelectableProductItem
import com.example.zippick.ui.model.dummy.sampleProducts
import com.example.zippick.util.LikedPreferences
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.MediumGray

@Composable
fun CategoryCompareScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val likedIds = remember { LikedPreferences.getLikedIds(context) }
    val likedProducts = remember { sampleProducts.filter { it.id.toString() in likedIds } }

    var selectedItems by remember { mutableStateOf<List<Product>>(emptyList()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // 버튼 영역 확보
        ) {
            Text(
                text = "비교를 원하는 상품을 선택해주세요 (최대 2개)",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(likedProducts) { product ->
                    val isSelected = selectedItems.contains(product)
                    SelectableProductItem(
                        product = product,
                        isSelected = isSelected,
                        onClick = {
                            selectedItems = when {
                                isSelected -> selectedItems - product
                                selectedItems.size < 2 -> selectedItems + product
                                else -> selectedItems
                            }
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                // TODO: 비교 화면 이동 등 처리
            },
            enabled = selectedItems.size == 2,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedItems.size == 2) MainBlue else MediumGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(text = "비교하기", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

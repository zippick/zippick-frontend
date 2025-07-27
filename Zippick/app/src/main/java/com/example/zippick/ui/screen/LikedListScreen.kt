package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.R
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.composable.compare.SelectableProductItem
import com.example.zippick.util.LikedPreferences
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.MediumGray
import android.util.Log

@Composable
fun LikedListScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val repository = remember { ProductRepository() }

    var likedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var selectedItems by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val likedIds = remember { LikedPreferences.getLikedIds(context).mapNotNull { it.toIntOrNull() } }

    LaunchedEffect(Unit) {
        try {
            likedProducts = repository.getLikedProducts(likedIds)
        } catch (e: Exception) {
            Log.e("LikedListScreen", "에러 발생: ${e.message}")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        if (likedProducts.isEmpty()) {
            // 찜한 상품이 없을 때
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty_heart),
                    contentDescription = "찜 아이콘",
                    modifier = Modifier
                        .size(96.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = "아직 찜한 상품이 없어요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "상품 비교 기능은 찜한 상품들로만\n이용하실 수 있어요.",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "관심 있는 상품에\n먼저 찜하기 버튼을 눌러보세요!",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // 찜한 상품 있을 때 기존 UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
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

            // 하단 버튼은 있을 때만 표시
            Button(
                onClick = {
                    val id1 = selectedItems[0].id
                    val id2 = selectedItems[1].id
                    navController.navigate("categoryCompareResult?id1=$id1&id2=$id2")
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
}

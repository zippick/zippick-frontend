package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.network.FakeProductsBySizeApi
import com.example.zippick.ui.composable.BottomBar
import com.example.zippick.ui.model.SizeSearchResultHolder
import com.example.zippick.ui.theme.MainBlue
import kotlinx.coroutines.launch

@Composable
fun SizeInputScreen(navController: NavHostController, category: String) {
    var width by remember { mutableStateOf("") }
    var depth by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val imageRes = when (category) {
        "의자" -> R.drawable.size_sample_chair
        "책상" -> R.drawable.size_sample_desk
        "소파" -> R.drawable.size_sample_sofa
        "식탁" -> R.drawable.size_sample_table
        "옷장" -> R.drawable.size_sample_closet
        "침대" -> R.drawable.size_sample_bed
        else -> R.drawable.size_sample_chair // 기본값
    }

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .imePadding() // 키보드 올라왔을 때 하단 영역 확보
                .verticalScroll(rememberScrollState()), // 스크롤 가능하게
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8F8F8)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = category,
                    modifier = Modifier.fillMaxSize(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "허용할 수 있는 최대 길이를 입력해주세요",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 사이즈 입력
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SizeInputRow(number = "1", label = "가로", value = width) { width = it }
                SizeInputRow(number = "2", label = "세로", value = depth) { depth = it }
                SizeInputRow(number = "3", label = "높이", value = height) { height = it }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (width.isNotBlank() && depth.isNotBlank() && height.isNotBlank()) {
                        coroutineScope.launch {
                            val (result, totalCount) = FakeProductsBySizeApi.getProductsBySize(
                                width = width.toInt(),
                                depth = depth.toInt(),
                                height = height.toInt(),
                                sort = "price_asc",
                                offset = 0
                            )
                            SizeSearchResultHolder.products = result
                            SizeSearchResultHolder.totalCount = totalCount
                            navController.navigate("sizeSearchResult")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "검색", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SizeInputRow(
    number: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = number,
            fontWeight = FontWeight.Bold,
            color = MainBlue,
            modifier = Modifier.width(20.dp),
            fontSize = 16.sp
        )
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(40.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    onValueChange(input)
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("길이 입력", fontSize = 14.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.ui.model.SizeSearchResultHolder
import com.example.zippick.ui.theme.DarkGray
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.Pretendard
import com.example.zippick.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun SizeInputScreen(navController: NavHostController, category: String, viewModel: ProductViewModel) {
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
        else -> R.drawable.size_sample_chair
    }

    Scaffold(
        containerColor = Color.White,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                color = DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                            viewModel.loadBySize(
                                width = width.toInt(),
                                depth = depth.toInt(),
                                height = height.toInt(),
                                sort = "price_asc",
                                offset = 0
                            )
                            SizeSearchResultHolder.products = viewModel.products.value
                            SizeSearchResultHolder.totalCount = viewModel.totalCount.value
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
            onValueChange = { input -> if (input.all { it.isDigit() }) onValueChange(input) },
            modifier = Modifier
                .weight(1f),
            placeholder = { Text("길이 입력", fontSize = 14.sp, color = DarkGray) },
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = Pretendard
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Text(
            text = "cm",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
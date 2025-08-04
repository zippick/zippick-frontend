package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.ui.theme.MainBlue

// 전역 선택 상태 유지용 변수
var selectedCategoryGlobal: String = "의자"

@Composable
fun CategorySelectionScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf(selectedCategoryGlobal) }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 40.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "어떤 상품을 찾으시나요?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            CategoryGrid(
                selectedCategory = selectedCategory,
                onCategorySelected = { selected ->
                    selectedCategory = selected
                    selectedCategoryGlobal = selected
                    navController.navigate("sizeInput/${selected}")
                }
            )
        }
    }
}

@Composable
fun CategoryGrid(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "의자" to R.drawable.category_chair,
        "책상" to R.drawable.category_desk,
        "소파" to R.drawable.category_sofa,
        "식탁" to R.drawable.category_table,
        "옷장" to R.drawable.category_closet,
        "침대" to R.drawable.category_bed
    )

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                rowItems.forEach { (label, imageRes) ->
                    CategoryItem(
                        label = label,
                        imageRes = imageRes,
                        selected = label == selectedCategory,
                        onClick = { onCategorySelected(label) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    label: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MainBlue else Color.LightGray

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        // 좌상단 텍스트
        Text(
            text = label,
            color = borderColor,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        // 중앙 이미지
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.85f) // 필요에 따라 이미지 크기 조정
        )
    }
}

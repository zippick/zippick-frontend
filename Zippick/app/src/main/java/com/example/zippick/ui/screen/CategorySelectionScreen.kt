package com.example.zippick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
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
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "어떤 상품을 찾으시나요?",
                fontSize = 16.sp,
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

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F8F8))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

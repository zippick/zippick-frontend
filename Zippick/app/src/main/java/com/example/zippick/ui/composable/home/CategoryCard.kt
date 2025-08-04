package com.example.zippick.ui.composable.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.zippick.ui.theme.DarkGray
import com.example.zippick.ui.theme.LightGray

@Composable
fun CategoryCard(
    categoryName: String,
    imageResId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(110.dp) // 카드 크기
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .border(1.5.dp, DarkGray, shape = RoundedCornerShape(12.dp))
            .background(LightGray, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        // 카테고리 텍스트 (좌상단)
        Text(
            text = categoryName,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        )
        // 이미지 (중앙)
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        )
    }
}

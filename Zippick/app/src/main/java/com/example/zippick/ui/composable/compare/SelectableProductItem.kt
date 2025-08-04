package com.example.zippick.ui.composable.compare

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.theme.LightGray
import com.example.zippick.ui.theme.MainBlue

@Composable
fun SelectableProductItem(
    product: Product,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = if (isSelected) MainBlue else LightGray,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 정사각형 비율 유지
        ) {
            // 제품 이미지 표시
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 제품 이름 텍스트
        androidx.compose.material3.Text(
            text = product.name,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 제품 가격 텍스트
        androidx.compose.material3.Text(
            text = "%,d원".format(product.price),
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        )
    }
}

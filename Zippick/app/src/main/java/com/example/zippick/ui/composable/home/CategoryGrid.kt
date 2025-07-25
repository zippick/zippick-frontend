package com.example.zippick.ui.composable.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.R

@Composable
fun CategoryGrid() {
    val categories = listOf(
        "의자" to R.drawable.chair,
        "책상" to R.drawable.desk,
        "소파" to R.drawable.sofa,
        "식탁" to R.drawable.table,
        "옷장" to R.drawable.closet,
        "침대" to R.drawable.bed
    )

    Column {
        for (row in categories.chunked(3)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                row.forEach { (label, image) ->
                    CategoryCard(
                        categoryName = label,
                        imageResId = image,
                        onClick = { /* TODO: 카테고리 선택 로직 */ }
                    )
                }
            }
        }
    }
}

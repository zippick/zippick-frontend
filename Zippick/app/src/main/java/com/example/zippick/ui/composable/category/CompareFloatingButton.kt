package com.example.zippick.ui.composable.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue

@Composable
fun CompareFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(70.dp)
            .shadow(8.dp, CircleShape)
            .background(color = MainBlue, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("상품", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("비교", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}


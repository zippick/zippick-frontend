package com.example.zippick.ui.composable.photo.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorPaletteSection(palette: List<Pair<String, String>>) {
    Text("인테리어 기반 색상 팔레트", fontWeight = FontWeight.Medium, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(16.dp))

    palette.forEach { (hex, label) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            val backgroundColor = parseHexColor(hex)
            val textColor = if (isColorDark(backgroundColor)) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = hex,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label)
        }
    }
}



fun parseHexColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}

fun isColorDark(color: Color): Boolean {
    val r = color.red * 255
    val g = color.green * 255
    val b = color.blue * 255
    val luminance = 0.299 * r + 0.587 * g + 0.114 * b
    return luminance < 186 // 일반적으로 이 기준 사용
}
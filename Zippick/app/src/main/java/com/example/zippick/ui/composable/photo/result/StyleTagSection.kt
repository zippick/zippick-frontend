package com.example.zippick.ui.composable.photo.result

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StyleTagSection(tags: List<String>) {
    Text("분석된 톤 & 스타일", fontWeight = FontWeight.Medium, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(16.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {
            Surface(
                modifier = Modifier
                    .border(BorderStroke(1.8.dp, MainBlue), shape = RoundedCornerShape(20.dp)), // <- 여기에 border 추가
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Text(
                    text = "# $it",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Medium,
                    color = MainBlue
                )
            }

        }
    }
}

package com.example.zippick.ui.composable.photo.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zippick.ui.composable.photo.AnalyzeButton

@Composable
fun RecommendButtons(
    onColorClick: () -> Unit,
    onStyleClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        AnalyzeButton(
            label = "색상 기반 추천",
            onClick = onColorClick,
            modifier = Modifier.weight(1f)
        )

        AnalyzeButton(
            label = "스타일 기반 추천",
            onClick = onStyleClick,
            modifier = Modifier.weight(1f)
        )
    }
}

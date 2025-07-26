package com.example.zippick.ui.composable.photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zippick.ui.theme.MainBlue

@Composable
fun AnalyzeButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.8.dp, MainBlue),
        shape = RoundedCornerShape(13.dp)
    ) {
        Text(text = label)
    }
}

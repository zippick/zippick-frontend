package com.example.zippick.ui.composable.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue

@Composable
fun FilterDropdownButton(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isActive) MainBlue else Color(0xFFF3F3F3)
    val textColor = if (isActive) Color.White else Color(0xFF333333)

    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 26.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(18.dp)
        )
    }
}

package com.example.zippick.ui.composable.category

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceFilterBottomSheet(
    sheetState: SheetState,
    minPrice: String,
    maxPrice: String,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("가격", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = minPrice,
                    onValueChange = {
                        // 숫자만 허용 + 0 이상만
                        if (it.all { ch -> ch.isDigit() } && (it.toIntOrNull() ?: 0) >= 0) {
                            onMinPriceChange(it)
                        }
                    },
                    label = { Text("최소 금액") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = maxPrice,
                    onValueChange = {
                        if (it.all { ch -> ch.isDigit() } && (it.toIntOrNull() ?: 0) >= 0) {
                            onMaxPriceChange(it)
                        }
                    },
                    label = { Text("최대 금액") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("초기화")
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("적용하기")
                }
            }
        }
    }
}

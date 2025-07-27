package com.example.zippick.ui.composable.sizeSearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zippick.ui.model.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortFilterForSizeBottomSheet(
    sheetState: SheetState,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("정렬 기준 선택", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            SortOption.entries.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSortChange(option)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedSort == option,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(option.label)
                }
            }
        }
    }
}

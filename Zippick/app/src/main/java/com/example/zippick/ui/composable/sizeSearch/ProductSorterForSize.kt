package com.example.zippick.ui.composable.sizeSearch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.composable.category.FilterToggle
import com.example.zippick.ui.composable.category.SortFilterBottomSheet
import com.example.zippick.ui.model.SortOption
import com.example.zippick.ui.theme.MainBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSorterForSize(
    productCount: Int,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isSortFilterChecked by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString {
                append("총 ")
                withStyle(style = SpanStyle(color = MainBlue)) {
                    append("$productCount")
                }
                append("개의 상품")
            },
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterToggle(
                label = "정렬",
                isChecked = isSortFilterChecked,
                onToggle = {
                    isSortFilterChecked = it
                    showSortSheet = it
                }
            )
        }
    }

    if (showSortSheet) {
        SortFilterBottomSheet(
            sheetState = sortSheetState,
            selectedSort = selectedSort,
            onSortChange = {
                onSortChange(it)
                coroutineScope.launch { sortSheetState.hide() }
                showSortSheet = false
                isSortFilterChecked = true
            },
            onDismiss = {
                coroutineScope.launch { sortSheetState.hide() }
                showSortSheet = false
            }
        )
    }
}

package com.example.zippick.ui.composable.category

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
import com.example.zippick.ui.model.SortOption
import com.example.zippick.ui.theme.MainBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilterHeader(
    productCount: Int,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    minPrice: String,
    maxPrice: String,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit,
    onPriceFilterApply: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val priceSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isPriceFilterChecked by remember { mutableStateOf(false) } // 토글 상태용
    var showPriceSheet by remember { mutableStateOf(false) }        // 바텀시트 표시용

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
                label = "가격",
                isChecked = isPriceFilterChecked,
                onToggle = {
                    isPriceFilterChecked = it
                    showPriceSheet = it
                }
            )

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

    if (showPriceSheet) {
        PriceFilterBottomSheet(
            sheetState = priceSheetState,
            minPrice = minPrice,
            maxPrice = maxPrice,
            onMinPriceChange = onMinPriceChange,
            onMaxPriceChange = onMaxPriceChange,
            onDismiss = {
                coroutineScope.launch { priceSheetState.hide() }
                showPriceSheet = false
            },
            onApply = {
                coroutineScope.launch { priceSheetState.hide() }
                showPriceSheet = false
                isPriceFilterChecked = true
                onPriceFilterApply()
            }
        )
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

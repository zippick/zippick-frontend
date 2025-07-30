package com.example.zippick.ui.composable.category

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    minPrice: String?,
    maxPrice: String?,
    onMinPriceChange: ((String) -> Unit)?,
    onMaxPriceChange: ((String) -> Unit)?,
    onPriceFilterApply: (() -> Unit)?
) {
    val coroutineScope = rememberCoroutineScope()
    val priceSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // 상태 저장: 가격 필터 토글 체크 여부 (0은 기본값으로 간주, 토글 OFF)
    var isPriceFilterChecked by rememberSaveable {
        mutableStateOf(
            (minPrice?.isNotBlank() == true && minPrice != "0") ||
                    (maxPrice?.isNotBlank() == true && maxPrice != "0")
        )
    }

    // 가격/정렬 바텀시트 표시 여부
    var showPriceSheet by rememberSaveable { mutableStateOf(false) }
    var showSortSheet by rememberSaveable { mutableStateOf(false) }

    // 빈 값으로 선언, 나중에 바텀시트 열릴 때 최신 값으로 덮어씀
    var tempMinPrice by rememberSaveable { mutableStateOf("") }
    var tempMaxPrice by rememberSaveable { mutableStateOf("") }

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
            if (onMinPriceChange != null && onMaxPriceChange != null && onPriceFilterApply != null) {
                FilterDropdownButton(
                    label = "가격",
                    isActive = isPriceFilterChecked,
                    onClick = {
                        tempMinPrice = minPrice ?: ""
                        tempMaxPrice = maxPrice ?: ""
                        showPriceSheet = true
                    }
                )
            }

            FilterDropdownButton(
                label = "정렬",
                isActive = selectedSort != SortOption.LATEST,
                onClick = {
                    showSortSheet = true
                }
            )
        }
    }

    // 가격 필터 바텀시트 표시
    if (showPriceSheet) {
        PriceFilterBottomSheet(
            sheetState = priceSheetState,
            minPrice = tempMinPrice,
            maxPrice = tempMaxPrice,
            onMinPriceChange = { tempMinPrice = it },
            onMaxPriceChange = { tempMaxPrice = it },
            onDismiss = {
                coroutineScope.launch { priceSheetState.hide() }
            },
            onApply = {
                coroutineScope.launch { priceSheetState.hide() }
                isPriceFilterChecked =
                    (tempMinPrice.isNotBlank() && tempMinPrice != "0") ||
                            (tempMaxPrice.isNotBlank() && tempMaxPrice != "0")
                onMinPriceChange?.invoke(tempMinPrice)
                onMaxPriceChange?.invoke(tempMaxPrice)
                onPriceFilterApply?.invoke()
            },
            onReset = {
                coroutineScope.launch { priceSheetState.hide() }
                isPriceFilterChecked = false
                tempMinPrice = ""
                tempMaxPrice = ""
                onMinPriceChange?.invoke("")
                onMaxPriceChange?.invoke("")
                onPriceFilterApply?.invoke()
            }
        )
    }

    // 바텀시트 닫히면 표시 상태 해제
    LaunchedEffect(priceSheetState.isVisible) {
        if (!priceSheetState.isVisible) {
            showPriceSheet = false
        }
    }

    // 정렬 필터 바텀시트
    if (showSortSheet) {
        SortFilterBottomSheet(
            sheetState = sortSheetState,
            selectedSort = selectedSort,
            onSortChange = {
                onSortChange(it)
                coroutineScope.launch { sortSheetState.hide() }
                showSortSheet = false
            },
            onDismiss = {
                coroutineScope.launch { sortSheetState.hide() }
                showSortSheet = false
            }
        )
    }
}

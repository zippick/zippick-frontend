package com.example.zippick.ui.composable.photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue
import androidx.compose.foundation.layout.Row as Row1

@Composable
fun CategorySelector(
    categoryList: List<String>,
    selectedCategory: MutableState<String?>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "구매를 원하는 상품을 알려주세요",
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            fontWeight = FontWeight.Medium
        )

        val chunkedList = categoryList.chunked(3)

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            chunkedList.forEach { rowItems ->
                Row1(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { category ->
                        val isSelected = category == selectedCategory.value

                        Surface(
                            color = if (isSelected) MainBlue else Color.White,
                            shape = RoundedCornerShape(13.dp),
                            border = BorderStroke(1.8.dp, MainBlue),
                            onClick = {
                                selectedCategory.value =
                                    if (isSelected) null else category // 선택 해제 or 교체
                            },
                            modifier = Modifier
                                .height(36.dp)
                                .weight(1f),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 15.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.White else MainBlue,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (rowItems.size < 3) {
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

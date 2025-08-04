package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zippick.ui.composable.compare.CompareRow
import com.example.zippick.ui.composable.compare.ProductColumn
import com.example.zippick.ui.viewmodel.CompareViewModel

@Composable
fun CategoryCompareScreen(
    navController: NavController,
    id1: Int,
    id2: Int
) {
    val viewModel: CompareViewModel = viewModel()

    val product1 by viewModel.product1
    val product2 by viewModel.product2

    LaunchedEffect(id1, id2) {
        viewModel.loadProducts(id1, id2)
    }

    // 데이터가 둘 다 로드된 경우에만 UI 렌더링
    if (product1 != null && product2 != null) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProductColumn(
                    product = product1!!,
                    modifier = Modifier.weight(1f)
                )
                ProductColumn(
                    product = product2!!,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            CompareRow("%,d원".format(product1!!.price), "가격", "%,d원".format(product2!!.price))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CompareRow("${product1!!.width}cm", "가로", "${product2!!.width}cm")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CompareRow("${product1!!.depth}cm", "세로", "${product2!!.depth}cm")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CompareRow("${product1!!.height}cm", "높이", "${product2!!.height}cm")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CompareRow(product1!!.color, "색상", product2!!.color)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CompareRow(product1!!.style, "스타일", product2!!.style)

        }
    }
}

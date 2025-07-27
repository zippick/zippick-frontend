package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zippick.ui.model.dummy.sampleProducts
import androidx.navigation.NavController as NavController1

@Composable
fun CategoryCompareScreen(
    navController: NavController1,
    id1: Int,
    id2: Int
) {
    val product1 = sampleProducts.find { it.id.toIntOrNull() == id1 }
    val product2 = sampleProducts.find { it.id.toIntOrNull() == id2 }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("상품 비교 결과", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            product1?.let {
                Text(text = it.name)
            }
            product2?.let {
                Text(text = it.name)
            }
        }

        // TODO: 가격, 이미지, 크기 등 비교 항목 추가
    }
}

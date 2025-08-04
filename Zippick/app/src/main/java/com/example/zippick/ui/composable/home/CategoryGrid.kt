package com.example.zippick.ui.composable.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.R

@Composable
fun CategoryGrid(navController: NavController) {
    val categories = listOf(
        "의자" to R.drawable.chair,
        "책상" to R.drawable.desk,
        "소파" to R.drawable.sofa,
        "식탁" to R.drawable.table,
        "옷장" to R.drawable.closet,
        "침대" to R.drawable.bed
    )

    Column {
        for (row in categories.chunked(3)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                row.forEach { (label, image) ->
                    CategoryCard(
                        categoryName = label,
                        imageResId = image,
                        onClick = {
                            val categoryParam = label
                            navController.navigate("category/${categoryParam}")
                        }
                    )
                }
            }
        }
    }
}

package com.example.zippick.ui.composable.compare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zippick.ui.model.ProductDetail

@Composable
fun ProductColumn(product: ProductDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = product.mainImageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .width(180.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = product.name, fontSize = 20.sp)
    }
}

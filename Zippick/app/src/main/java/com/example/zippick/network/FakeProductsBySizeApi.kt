package com.example.zippick.network

import com.example.zippick.ui.model.Product
import kotlinx.coroutines.delay

object FakeProductsBySizeApi {
    suspend fun getProductsBySize(
        width: Int,
        depth: Int,
        height: Int,
        sort: String,
        offset: Int
    ): Pair<List<Product>, Int> {
        delay(1000)

        val products = listOf(
            Product("1", "원목책상", 9900, "https://cdn.011st.com/11dims/resize/1000x1000/quality/75/11src/product/1854552772/B.jpg?445000000"),
            Product("2", "강철책상", 9900, "https://cdn.011st.com/11dims/resize/1000x1000/quality/75/11src/product/1854552772/B.jpg?445000000"),
            Product("3", "고급책상", 15000, "https://cdn.011st.com/11dims/resize/1000x1000/quality/75/11src/product/1854552772/B.jpg?445000000")
        )

        return Pair(products, 160)
    }
}

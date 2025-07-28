package com.example.zippick.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.InteriorRepository
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.model.RecommendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoRecommendViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun fetchRecommendations(category: String, type: String, values: List<String>) {
        viewModelScope.launch {
            try {
                val request = if (type.uppercase() == "COLOR") {
                    RecommendRequest(
                        category = category,
                        recommendType = "COLOR",
                        toneCategories = values,
                        tags = emptyList()
                    )
                } else {
                    RecommendRequest(
                        category = category,
                        recommendType = "STYLE",
                        toneCategories = emptyList(),
                        tags = values
                    )
                }

                val result = InteriorRepository.postRecommendProduct(request)

                Log.d("RECOMMEND", "요청 category: ${request.category}")
                Log.d("RECOMMEND", "요청 type: ${request.recommendType}")
                Log.d("RECOMMEND", "요청 toneCategories: ${request.toneCategories}")
                Log.d("RECOMMEND", "요청 tags: ${request.tags}")
                Log.d("RECOMMEND", "서버 응답: ${result}")


                _products.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

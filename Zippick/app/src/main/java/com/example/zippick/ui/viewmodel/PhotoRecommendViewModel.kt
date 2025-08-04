package com.example.zippick.ui.viewmodel

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchRecommendations(category: String, type: String, values: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true // 로딩 시작

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

                _products.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false // 로딩 종료
            }
        }
    }

}

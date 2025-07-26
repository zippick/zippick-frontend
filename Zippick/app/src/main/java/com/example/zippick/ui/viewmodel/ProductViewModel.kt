package com.example.zippick.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _totalCount = MutableStateFlow(0)
    val totalCount: StateFlow<Int> = _totalCount

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _aiImageUrl = MutableStateFlow<String?>(null)
    val aiImageUrl: StateFlow<String?> = _aiImageUrl

    fun loadBySize(width: Int, depth: Int, height: Int, sort: String, offset: Int) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val response = repository.getProductsBySize(width, depth, height, sort, offset)
                println("API 응답: products=${response.products.size}, totalCount=${response.totalCount}")
                _products.value = response.products
                _totalCount.value = response.totalCount
            } catch (e: Exception) {
                _errorMessage.value = "사이즈 조회 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun requestAiLayout(request: AiLayoutRequest) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val response = repository.postAiLayout(request)
                _aiImageUrl.value = response.resultImageUrl
            } catch (e: Exception) {
                _errorMessage.value = "AI 배치 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}

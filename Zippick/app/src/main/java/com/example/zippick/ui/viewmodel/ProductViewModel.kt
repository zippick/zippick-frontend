package com.example.zippick.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.*
import com.example.zippick.ui.screen.selectedCategoryGlobal
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

    private var currentSort: String = "price_asc"
    private var currentOffset: Int = 0
    private var lastRequest: Triple<Int, Int, Int>? = null

    fun loadBySize(width: Int, depth: Int, height: Int, sort: String, offset: Int, append: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val response = repository.getProductsBySize(selectedCategoryGlobal, width, depth, height, sort, offset)
                if (append) {
                    _products.value = _products.value + response.products
                } else {
                    _products.value = response.products
                }
                _totalCount.value = response.totalCount
                currentSort = sort
                currentOffset = offset + response.products.size
                lastRequest = Triple(width, depth, height)
            } catch (e: Exception) {
                _errorMessage.value = "사이즈 조회 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun reloadWithSort(sort: String) {
        lastRequest?.let { (width, depth, height) ->
            currentOffset = 0
            loadBySize(width, depth, height, sort, offset = 0, append = false)
        }
    }

    fun loadMore() {
        lastRequest?.let { (width, depth, height) ->
            loadBySize(width, depth, height, currentSort, offset = currentOffset, append = true)
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

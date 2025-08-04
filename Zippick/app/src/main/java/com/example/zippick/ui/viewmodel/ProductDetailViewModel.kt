package com.example.zippick.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.ProductDetail
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {

    private val repository = ProductRepository() // 내부에서 직접 생성

    private var _productDetail = mutableStateOf<ProductDetail?>(null)
    val productDetail: State<ProductDetail?> = _productDetail

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                _productDetail.value = repository.getProductDetail(id)
            } catch (e: Exception) {
            }
        }
    }
}

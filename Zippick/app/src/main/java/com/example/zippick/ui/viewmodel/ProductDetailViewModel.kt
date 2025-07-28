package com.example.zippick.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.ProductDetail
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ProductDetailViewModel : ViewModel() {

    private val repository = ProductRepository() // 내부에서 직접 생성

    private var _productDetail = mutableStateOf<ProductDetail?>(null)
    val productDetail: State<ProductDetail?> = _productDetail

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                _productDetail.value = repository.getProductDetail(id)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "상품 정보 불러오기 실패: ${e.message}")
            }
        }
    }
}

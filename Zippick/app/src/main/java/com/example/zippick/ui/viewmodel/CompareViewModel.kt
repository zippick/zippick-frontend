package com.example.zippick.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.ProductDetail
import kotlinx.coroutines.launch

class CompareViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _product1 = mutableStateOf<ProductDetail?>(null)
    val product1: State<ProductDetail?> = _product1

    private val _product2 = mutableStateOf<ProductDetail?>(null)
    val product2: State<ProductDetail?> = _product2

    fun loadProducts(id1: Int, id2: Int) {
        viewModelScope.launch {
            try {
                _product1.value = repository.getProductDetail(id1)
                _product2.value = repository.getProductDetail(id2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

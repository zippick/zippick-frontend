package com.example.zippick.ui.viewmodel;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.order.OrderRepository
import com.example.zippick.ui.model.OrderRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _orderResult = MutableStateFlow<String?>(null)
    val orderResult: StateFlow<String?> = _orderResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun postOrder(request: OrderRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.postOrder(request)
                _orderResult.value = "주문이 정상적으로 완료되었습니다."
            } catch (e: Exception) {
                val msg = "주문 실패: ${e.message}"
                _orderResult.value = msg
                android.util.Log.e("OrderViewModel", msg, e) // 로그 추가

            } finally {
                _loading.value = false
            }
        }
    }

    fun clearOrderResult() {
        _orderResult.value = null
    }
}
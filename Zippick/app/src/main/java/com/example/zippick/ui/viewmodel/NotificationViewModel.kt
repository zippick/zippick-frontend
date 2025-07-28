package com.example.zippick.ui.viewmodel

import com.example.zippick.network.notification.NotificationRepository
import com.example.zippick.network.notification.NotificationResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()

    private val _notifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val notifications: StateFlow<List<NotificationResponse>> = _notifications

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadNotifications(offset: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getNotifications(offset)
                _notifications.value = response.notifications
            } catch (e: Exception) {
                // 에러처리(로깅 등) 필요시 추가
            } finally {
                _loading.value = false
            }
        }
    }
}
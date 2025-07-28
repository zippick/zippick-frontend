package com.example.zippick.ui.viewmodel

import com.example.zippick.network.notification.NotificationRepository
import com.example.zippick.network.notification.NotificationResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.notification.NotificationSendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()

    private val _notifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val notifications: StateFlow<List<NotificationResponse>> = _notifications

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadNotifications(
        offset: Int,
        append: Boolean = false,
        onLoaded: ((List<NotificationResponse>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getNotifications(offset)
                val list = response?.notifications ?: emptyList()
                if (append) {
                    _notifications.value = _notifications.value + list
                } else {
                    _notifications.value = list
                }
                onLoaded?.invoke(list)
            } catch (e: Exception) {
                if (!append) _notifications.value = emptyList()
                onLoaded?.invoke(emptyList())
            } finally {
                _loading.value = false
            }
        }
    }

    fun sendNotification(request: NotificationSendRequest) {
        viewModelScope.launch {
            try {
                repository.postNotifications(request)
                // 성공 처리 필요시 추가(예: 토스트, 새로고침 등)
            } catch (e: Exception) {
                // 에러처리(로깅, 알림 등)
            }
        }
    }
}
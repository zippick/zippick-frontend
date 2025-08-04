package com.example.zippick.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.notification.NotificationRepository
import com.example.zippick.network.notification.NotificationResponse
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

    fun loadNotificationsAll(
        onLoaded: ((List<NotificationResponse>) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getNotifications()
                val list = response?.notifications ?: emptyList()
                _notifications.value = list
                onLoaded?.invoke(list)
            } catch (e: Exception) {
                _notifications.value = emptyList()
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
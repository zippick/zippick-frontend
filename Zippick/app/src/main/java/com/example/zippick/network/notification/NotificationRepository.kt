package com.example.zippick.network.notification

import com.example.zippick.network.RetrofitInstance


class NotificationRepository {
    private val api = RetrofitInstance.retrofit.create(NotificationApi::class.java)

    suspend fun getNotifications(): NotificationListResponse {
        return api.getNotifications()
    }

    suspend fun postNotifications(request: NotificationSendRequest): Unit {
        api.postNotifications(request)
    }
}
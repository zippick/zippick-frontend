package com.example.zippick.network.notification

import com.example.zippick.network.RetrofitInstance


class NotificationRepository {
    private val api = RetrofitInstance.retrofit.create(NotificationApi::class.java)

    suspend fun getNotifications(offset: Int): NotificationListResponse {
        return api.getNotifications(offset)
    }
}
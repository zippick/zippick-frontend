package com.example.zippick.network.alert;

import com.example.zippick.ui.screen.NotificationItem
import retrofit2.http.GET

interface ApiService {
    @GET("/notifications")
    suspend fun getNotifications(): List<NotificationItem>
}
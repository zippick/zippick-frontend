package com.example.zippick.network.notification;

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 서버 응답 전체(wrapper)
data class NotificationListResponse(
    val notifications: List<NotificationResponse>
)

// 실제 알림 1개 정보
data class NotificationResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String
)

data class NotificationSendRequest(
    val title: String,
    val content: String,
    val createdAt: String
)

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): NotificationListResponse

    @POST("api/notifications/send")
    suspend fun postNotifications(
        @Body request: NotificationSendRequest
    ): Unit
}

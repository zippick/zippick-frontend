package com.example.zippick.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.zippick.MainActivity
import com.example.zippick.R
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.fcm.FcmApi
import com.example.zippick.network.fcm.dto.FcmTokenRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitInstance.retrofit.create(FcmApi::class.java)
                val response = api.registerToken(FcmTokenRequest(fcmToken = token))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = remoteMessage.notification
        if (notification != null) {
            val title = notification.title ?: "제목 없음"
            val body = notification.body ?: "내용 없음"

            // 알림 클릭 시 MainActivity로 이동 + "navigateTo" 전달
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("navigateTo", "notifications")
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_logo) // 알림 아이콘
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // 알림 클릭 시 이동 설정
                .setAutoCancel(true)            // 클릭 후 알림 제거

            val notificationId = System.currentTimeMillis().toInt()

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            }
        }
    }

}

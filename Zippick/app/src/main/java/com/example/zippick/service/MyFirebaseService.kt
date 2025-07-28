package com.example.zippick.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.zippick.R
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.fcm.FcmApi
import com.example.zippick.network.fcm.dto.FcmTokenRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "새 FCM 토큰 수신됨: $token")

        // 서버로 전송 (로그인된 사용자라고 가정하고 실행)
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitInstance.retrofit.create(FcmApi::class.java)
                val response = api.registerToken(FcmTokenRequest(fcmToken = token))
                Log.d("FCM", "서버 응답: ${response.success}")
            } catch (e: Exception) {
                Log.e("FCM", "서버 전송 실패: ${e.message}")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "💬 수신한 알림: ${remoteMessage.notification?.title}")
        val notification = remoteMessage.notification
        if (notification != null) {
            val title = notification.title ?: "제목 없음"
            val body = notification.body ?: "내용 없음"

            val builder = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_notification) // drawable에 반드시 존재해야 함
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notificationId = System.currentTimeMillis().toInt()

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            } else {
                Log.w("FCM", "알림 권한이 없어 알림을 표시하지 않음")
            }

        }
    }
}

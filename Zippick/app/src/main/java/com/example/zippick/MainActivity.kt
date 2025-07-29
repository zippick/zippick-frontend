package com.example.zippick

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.fcm.FcmApi
import com.example.zippick.network.fcm.dto.FcmTokenRequest
import com.example.zippick.network.TokenManager
import com.example.zippick.ui.composable.MainScreenWithBottomNav
import com.example.zippick.ui.theme.AndroidLabTheme
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)

        enableEdgeToEdge()

        // Android 13 이상: 알림 권한 요청
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (checkSelfPermission(permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 1001)
            }
        }

        setContent {
            AndroidLabTheme {
                val navController = rememberNavController()

                // 알림 클릭 여부 판단
                val navigateTo = intent?.getStringExtra("navigateTo")

                // 항상 홈으로 시작
                MainScreenWithBottomNav(
                    navController = navController,
                    startDestination = "home"
                )

                // 알림 클릭 시, NavHost 초기화 후 알림화면으로 이동
                LaunchedEffect(Unit) {
                    if (navigateTo == "notifications") {
                        navController.navigate("notifications")
                    }
                }
            }
        }
    }

}


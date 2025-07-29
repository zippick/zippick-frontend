package com.example.zippick.ui.composable

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.zippick.network.TokenManager

@Composable
fun RequireLogin(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    // 상태값으로 token을 remember하여 렌더링 시점 제어
    var token by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val fetchedToken = TokenManager.getToken()
        Log.d("ZIPPICK", "🔐 RequireLogin: fetchedToken = $fetchedToken")
        token = fetchedToken
        if (fetchedToken.isNullOrBlank() || fetchedToken == "null") {
            Log.w("ZIPPICK", "🚨 RequireLogin: Token invalid → navigating to login")
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        } else {
            Log.d("ZIPPICK", "✅ RequireLogin: Token is valid")
        }
    }

    // ✅ 아직 토큰 확인 전이면 아무것도 그리지 않음 (플리커 방지)
    if (token == null) return

    // ✅ 로그인 상태일 때만 콘텐츠 렌더링
    if (token != "null" && token!!.isNotBlank()) {
        content()
    }
}
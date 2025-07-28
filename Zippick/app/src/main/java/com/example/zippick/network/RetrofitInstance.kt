package com.example.zippick.network

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 토큰 관리 객체
object TokenManager {
    private const val PREFS_NAME = "zippick_prefs"
    private const val TOKEN_KEY = "auth_token"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
//        return prefs.getString(TOKEN_KEY, null)
        return "Bearer 55c5982d-1641-49f3-be64-8231f468e96c"
    }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.getToken()
        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        // 타임아웃은 기본 60초
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(TokenInterceptor()) // 토큰 자동 삽입
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
//            .baseUrl("https://zippick.n-e.kr/")
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

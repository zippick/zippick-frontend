package com.example.zippick.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 토큰 관리 객체
object TokenManager {
    //var token: String? = null
    //TODO: 아래는 임시로 하드코딩한 토큰값입니다. 배포시엔 바꿔주세요
    var token: String? = "여기에 넣어주세요"
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.token
        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor()) // 토큰 자동 삽입
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://zippick.n-e.kr/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

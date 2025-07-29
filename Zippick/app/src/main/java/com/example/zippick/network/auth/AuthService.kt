    package com.example.zippick.network.auth

    import com.example.zippick.ui.model.LoginRequest
    import com.example.zippick.ui.model.LoginResponse
    import retrofit2.http.Body
    import retrofit2.http.POST

    interface AuthService {
        @POST("/api/auth/login")
        suspend fun login(@Body request: LoginRequest): LoginResponse
    }

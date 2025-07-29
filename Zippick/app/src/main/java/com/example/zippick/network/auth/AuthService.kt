    package com.example.zippick.network.auth

    import com.example.zippick.ui.model.LoginRequest
    import com.example.zippick.ui.model.LoginResponse
    import retrofit2.http.Body
    import retrofit2.http.Header
    import retrofit2.http.POST
    import retrofit2.Response

    interface AuthService {
        @POST("/api/auth/login")
        suspend fun login(@Body request: LoginRequest): LoginResponse

        @POST("/api/auth/logout")
        suspend fun logout(@Header("token") token: String): Response<Void>
    }

package com.example.zippick.network

import android.net.Uri
import com.example.zippick.ui.model.dummy.AiCombineRequest
import com.example.zippick.ui.model.dummy.AiCombineResponse
import kotlinx.coroutines.delay

object FakeAiCombineApi {
    suspend fun postComposeRequest(
        roomImageUri: Uri,
        request: AiCombineRequest
    ): AiCombineResponse {
        delay(2000) // 실제 네트워크 딜레이 모사
        return AiCombineResponse(
            resultImageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c\n"
        )
    }
}

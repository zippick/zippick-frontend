package com.example.zippick.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.InteriorRepository
import com.example.zippick.util.FileUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PhotoAnalysisViewModel : ViewModel() {
    var palette by mutableStateOf<List<Triple<String, String, String>>>(emptyList())
        private set
    var tags by mutableStateOf<List<String>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun analyzeImage(context: Context, imageUri: Uri, category: String) {
        if (palette.isNotEmpty() && tags.isNotEmpty()) return  // 재요청 방지

        viewModelScope.launch {
            isLoading = true
            try {
                val file = FileUtil.getFileFromUri(context, imageUri)
                val categoryRequest = category.toRequestBody("text/plain".toMediaTypeOrNull())
                val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("roomImage", file.name, imageRequestBody)

                val response = InteriorRepository.postAiInterior(imagePart)
                palette = response.palette.map {
                    Triple(it.colorCode, it.colorName, it.toneCategory)
                }
                tags = response.tags
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun clearResult() {
        palette = emptyList()
        tags = emptyList()
    }
}

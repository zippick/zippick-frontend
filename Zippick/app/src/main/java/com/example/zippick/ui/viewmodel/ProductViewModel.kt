package com.example.zippick.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippick.network.product.ProductRepository
import com.example.zippick.ui.model.*
import com.example.zippick.ui.screen.selectedCategoryGlobal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _totalCount = MutableStateFlow(0)
    val totalCount: StateFlow<Int> = _totalCount

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _aiImageUrl = MutableStateFlow<String?>(null)
    val aiImageUrl: StateFlow<String?> = _aiImageUrl

    private var currentSort: String = "price_asc"
    private var currentOffset: Int = 0
    private var lastRequest: Triple<Int, Int, Int>? = null

    fun loadBySize(width: Int, depth: Int, height: Int, sort: String, offset: Int, append: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val response = repository.getProductsBySize(selectedCategoryGlobal, width, depth, height, sort, offset)
                if (append) {
                    _products.value = _products.value + response.products
                } else {
                    _products.value = response.products
                }
                _totalCount.value = response.totalCount
                currentSort = sort
                currentOffset = offset + response.products.size
                lastRequest = Triple(width, depth, height)
            } catch (e: Exception) {
                _errorMessage.value = "사이즈 조회 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun reloadWithSort(sort: String) {
        lastRequest?.let { (width, depth, height) ->
            currentOffset = 0
            loadBySize(width, depth, height, sort, offset = 0, append = false)
        }
    }

    fun loadMore() {
        lastRequest?.let { (width, depth, height) ->
            loadBySize(width, depth, height, currentSort, offset = currentOffset, append = true)
        }
    }

    // AI 배치 요청 함수
    fun requestAiLayout(
        imageUri: Uri,
        furnitureImageUrl: String,
        category: String,
        context: Context
    ) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri)
                val fileBytes = inputStream?.readBytes()
                inputStream?.close()

                val requestFile = fileBytes?.let {
                    RequestBody.create("image/*".toMediaTypeOrNull(), it)
                }

                val multipartImage = requestFile?.let {
                    MultipartBody.Part.createFormData("roomImage", "photo.jpg", it)
                } ?: throw IllegalArgumentException("사진을 선택해주세요")

                val imageUrlPart = furnitureImageUrl.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = repository.postAiLayout(
                    roomImage = multipartImage,
                    furnitureImageUrl = imageUrlPart,
                    category = categoryPart
                )

                _aiImageUrl.value = response.resultImageUrl

            } catch (e: Exception) {
                _errorMessage.value = "AI 배치 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}

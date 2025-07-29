package com.example.zippick.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
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
import com.example.zippick.util.convertToParam

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _categorySortOption = MutableStateFlow(SortOption.LATEST)
    val categorySortOption: StateFlow<SortOption> = _categorySortOption

    private val _sizeSearchSortOption = MutableStateFlow(SortOption.LATEST)
    val sizeSearchSortOption: StateFlow<SortOption> = _sizeSearchSortOption

    fun setCategorySortOption(option: SortOption) {
        _categorySortOption.value = option
    }

    fun setSizeSearchSortOption(option: SortOption) {
        _sizeSearchSortOption.value = option
    }

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

    private var currentKeyword: String? = null
    private var currentCategory: String = "전체"
    private var currentMinPrice: String? = null
    private var currentMaxPrice: String? = null

    // 사이즈 기반 조회
    fun loadBySize(width: Int, depth: Int, height: Int, sort: String, offset: Int, append: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val categoryParam = convertToParam(selectedCategoryGlobal)
                val response = repository.getProductsBySize(categoryParam, width, depth, height, sort, offset)
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
        if (_loading.value) return
        if (_products.value.size >= _totalCount.value) return

        lastRequest?.let { (width, depth, height) ->
            loadBySize(width, depth, height, currentSort, offset = currentOffset, append = true)
        }
    }

    // 카테고리 + 가격 범위 기반 조회
    fun loadByCategoryAndPrice(
        category: String,
        minPrice: String?,
        maxPrice: String?,
        sort: String,
        offset: Int,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val categoryParam = if (category == "전체") "all" else convertToParam(category)
                val min = minPrice?.takeIf { it.isNotBlank() }?.toLongOrNull() ?: 0L
                val max = maxPrice?.takeIf { it.isNotBlank() }?.toLongOrNull()

                val result = repository.getProductsByCategoryAndPrice(
                    category = categoryParam,
                    minPrice = min,
                    maxPrice = max,
                    sort = sort,
                    offset = offset
                )

                if (append) {
                    _products.value += result.products
                } else {
                    _products.value = result.products
                }
                _totalCount.value = result.totalCount

                currentCategory = category
                currentMinPrice = minPrice
                currentMaxPrice = maxPrice
                currentSort = sort
                currentOffset = offset + result.products.size

            } catch (e: Exception) {
                _errorMessage.value = "카테고리 검색 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMoreByCategoryAndPrice() {
        if (_loading.value) return
        if (_products.value.size >= _totalCount.value) return

        loadByCategoryAndPrice(
            category = currentCategory,
            minPrice = currentMinPrice,
            maxPrice = currentMaxPrice,
            sort = currentSort,
            offset = currentOffset,
            append = true
        )
    }

    fun requestAiLayout(
        imageUri: Uri,
        furnitureImageUrl: String,
        category: String,
        context: Context
    ) {
        Log.d("AI_LAYOUT", "requestAiLayout() 시작됨")

        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri)
                val fileBytes = inputStream?.readBytes()
                inputStream?.close()

                Log.d("AI_LAYOUT", "이미지 파일 바이트 읽기 완료")

                val requestFile = fileBytes?.let {
                    RequestBody.create("image/*".toMediaTypeOrNull(), it)
                }

                val multipartImage = requestFile?.let {
                    MultipartBody.Part.createFormData("roomImage", "photo.jpg", it)
                } ?: throw IllegalArgumentException("사진을 선택해주세요")

                val imageUrlPart = furnitureImageUrl.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

                Log.d("AI_LAYOUT", "API 호출 직전")

                val response = repository.postAiLayout(
                    roomImage = multipartImage,
                    furnitureImageUrl = imageUrlPart,
                    category = categoryPart
                )

                Log.d("AI_LAYOUT", "API 응답 성공! resultImageUrl = ${response.resultImageUrl}") // ✅ 3단계 로그

                _aiImageUrl.value = response.resultImageUrl

            } catch (e: Exception) {
                Log.e("AI_LAYOUT", "API 호출 실패: ${e.message}", e)
                _errorMessage.value = "AI 배치 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // 키워드 기반 검색 함수
    fun searchProductsByKeyword(keyword: String, sort: String, offset: Int, append: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val response = repository.getProductsByKeyword(keyword, sort, offset)
                if (append) {
                    _products.value = _products.value + response.products
                } else {
                    _products.value = response.products
                }
                _totalCount.value = response.totalCount
                currentKeyword = keyword
                currentSort = sort
                currentOffset = offset + response.products.size
            } catch (e: Exception) {
                _errorMessage.value = "검색 실패: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMoreProducts() {
        if (_loading.value) return
        if (_products.value.size >= _totalCount.value) return

        currentKeyword?.let {
            searchProductsByKeyword(
                keyword = it,
                sort = currentSort,
                offset = currentOffset,
                append = true
            )
        }
    }
}

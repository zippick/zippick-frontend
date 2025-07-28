package com.example.zippick.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.ui.composable.photo.result.ColorPaletteSection
import com.example.zippick.ui.composable.photo.result.ImageHeader
import com.example.zippick.ui.composable.photo.result.RecommendButtons
import com.example.zippick.ui.composable.photo.result.StyleTagSection
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.zippick.network.product.InteriorRepository
import com.example.zippick.ui.composable.photo.LottieLoading
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.util.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun PhotoAnalysisResultScreen(
    navController: NavController,
    imageUri: Uri,
    category: String
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 응답 상태
    var palette by remember { mutableStateOf<List<Triple<String, String, String>>>(emptyList()) }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        Log.d("AI_ANALYZE", "로딩 시작")
        Log.d("AI_ANALYZE", "카테고리: $category")
        try {
            val file = FileUtil.getFileFromUri(context, imageUri)
            Log.d("AI_ANALYZE", "파일 경로: ${file.absolutePath}")

            val categoryRequest = category.toRequestBody("text/plain".toMediaTypeOrNull())
            val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("roomImage", file.name, imageRequestBody)

            val response = InteriorRepository.postAiInterior(imagePart)
            Log.d("AI_ANALYZE", "서버 응답 palette: ${response.palette}")
            Log.d("AI_ANALYZE", "서버 응답 tags: ${response.tags}")

            palette = response.palette.map {
                Triple(it.colorCode, it.colorName, it.toneCategory)
            }
            tags = response.tags
        } catch (e: Exception) {
            Log.e("AI_ANALYZE", "에러 발생: ${e.message}", e)
        } finally {
            isLoading = false
            Log.d("AI_ANALYZE", "로딩 종료")
        }
    }

    if (isLoading) {
        // 로딩 중 화면
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieLoading(modifier = Modifier.size(120.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "이미지를 분석중입니다",
                    color = MainBlue,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "잠시만 기다려주세요!",
                    color = MainBlue,
                    fontSize = 16.sp
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // 1. 이미지 영역
            ImageHeader(imageUri)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // 2. 색상 코드
                ColorPaletteSection(palette.map { it.first to it.second })

                Spacer(modifier = Modifier.height(50.dp))

                // 3. 해시태그 스타일
                StyleTagSection(tags)

                Spacer(modifier = Modifier.height(50.dp))

                // 4. 추천 버튼
                val colorToneSet = palette.map { it.third }.toSet()
                val tagList = tags.toSet()
                RecommendButtons(
                    onColorClick = {
                        val values = Uri.encode(colorToneSet.joinToString(","))
                        val encodedCategory = Uri.encode(category)
                        navController.navigate("photoRecommendList/$encodedCategory/color/$values")
                    },
                    onStyleClick = {
                        val values = Uri.encode(tagList.joinToString(","))
                        val encodedCategory = Uri.encode(category)
                        navController.navigate("photoRecommendList/$encodedCategory/style/$values")
                    }
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}


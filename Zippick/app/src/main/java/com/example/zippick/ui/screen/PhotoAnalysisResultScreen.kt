package com.example.zippick.ui.screen

import android.net.Uri
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.ui.composable.photo.result.ColorPaletteSection
import com.example.zippick.ui.composable.photo.result.ImageHeader
import com.example.zippick.ui.composable.photo.result.RecommendButtons
import com.example.zippick.ui.composable.photo.result.StyleTagSection
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.composable.photo.LottieLoading
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.viewmodel.PhotoAnalysisViewModel

@Composable
fun PhotoAnalysisResultScreen(
    navController: NavController,
    imageUri: Uri,
    category: String,
    viewModel: PhotoAnalysisViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val palette = viewModel.palette
    val tags = viewModel.tags
    val isLoading = viewModel.isLoading

    // 이전 결과가 없을 경우에만 분석 수행
    LaunchedEffect(imageUri, category) {
        viewModel.analyzeImage(context, imageUri, category)
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
                LottieLoading(modifier = Modifier.size(90.dp))

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
                Spacer(modifier = Modifier.height(20.dp))

                // 2. 색상 코드
                ColorPaletteSection(palette.map { it.first to it.second })

                Spacer(modifier = Modifier.height(30.dp))

                // 3. 해시태그 스타일
                StyleTagSection(tags)

                Spacer(modifier = Modifier.height(30.dp))

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
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


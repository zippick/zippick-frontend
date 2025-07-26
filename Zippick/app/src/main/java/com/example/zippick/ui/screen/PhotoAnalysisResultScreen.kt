package com.example.zippick.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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

@Composable
fun PhotoAnalysisResultScreen(
    navController: NavController,
    imageUri: Uri
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 응답 상태
    var palette by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 임시 데이터 주입
    LaunchedEffect(Unit) {
        palette = listOf(
            "#E4D9C2" to "우드 베이지",
            "#B1956C" to "딥 샌드",
            "#F5F5F5" to "뉴트럴 화이트"
        )

        tags = listOf("내추럴", "미니멀", "우드톤")

        isLoading = false
    }

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
            ColorPaletteSection(palette)

            Spacer(modifier = Modifier.height(50.dp))

            // 3. 해시태그 스타일
            StyleTagSection(tags)

            Spacer(modifier = Modifier.height(50.dp))

            // 4. 추천 버튼
            RecommendButtons(
                onColorClick = { /* TODO */ },
                onStyleClick = { /* TODO */ }
            )
        }
    }
}


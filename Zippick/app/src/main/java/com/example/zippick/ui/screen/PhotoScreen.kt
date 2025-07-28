package com.example.zippick.ui.screen

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippick.R
import com.example.zippick.ui.composable.photo.AnalyzeButton
import com.example.zippick.ui.composable.photo.CategorySelector
import com.example.zippick.ui.composable.photo.PhotoUploadSection

@Composable
fun PhotoScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val selectedCategory = remember { mutableStateOf<String?>("의자") }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val categoryList = listOf("의자", "소파", "식탁", "책상", "옷장", "침대")
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 갤러리/파일 탐색기 런처
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedImageUri.value = it }
    }

    // 카메라 런처
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri?.let { selectedImageUri.value = it }
        }
    }

    fun launchImageChooser() {
        val options = listOf("카메라", "갤러리", "파일 탐색기")

        // 예: 간단한 AlertDialog 또는 BottomSheet로 선택 UI 표시
        // 여기서는 임시로 갤러리 런처 실행만 예시로
        galleryLauncher.launch("image/*")
    }

    fun launchCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "새 사진")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ai_recommand),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CategorySelector(categoryList, selectedCategory)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "상품을 놓을 공간의 사진을 업로드 해주세요",
                modifier = Modifier.padding(top = 16.dp, bottom = 10.dp),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            PhotoUploadSection(
                selectedImageUri = selectedImageUri.value,
                onPickImageClick = { launchImageChooser() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnalyzeButton(
                label = "분석하기",
                onClick = {
                val imageUriEncoded = Uri.encode(selectedImageUri.value.toString())
                val categoryEncoded = Uri.encode(selectedCategory.value ?: "")
                navController.navigate("photoAnalysis/$imageUriEncoded/$categoryEncoded")
            })

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

package com.example.zippick.ui.screen

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // 사진 선택 결과를 받아 처리할 런처
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data ?: cameraImageUri
            if (uri != null) {
                selectedImageUri.value = uri
            }
        }
    }

    // 카메라용 이미지 URI 생성 함수
    fun createImageUri(): Uri {
        val timeStamp = System.currentTimeMillis()
        val fileName = "camera_photo_$timeStamp.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Zippick")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }

    // 카메라 / 갤러리 / 파일 탐색기 선택 창 띄우는 함수
    fun launchImageChooser() {
        val imageUri = createImageUri()
        cameraImageUri = imageUri

        val cameraIntent = android.content.Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }

        val galleryIntent = android.content.Intent(android.content.Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        val fileExplorerIntent = android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val chooser = android.content.Intent.createChooser(galleryIntent, "사진 업로드")
        chooser.putExtra(
            android.content.Intent.EXTRA_INITIAL_INTENTS,
            arrayOf(cameraIntent, fileExplorerIntent)
        )
        launcher.launch(chooser)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ai_recommand),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
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
                onPickImageClick = { launchImageChooser() } // 이 버튼 클릭 시 chooser 띄움
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnalyzeButton(
                label = "분석하기",
                onClick = {
                    val imageUriEncoded = Uri.encode(selectedImageUri.value.toString())
                    val categoryEncoded = Uri.encode(selectedCategory.value ?: "")
                    navController.navigate("photoAnalysis/$imageUriEncoded/$categoryEncoded")
                },
                enabled = selectedImageUri.value != null
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


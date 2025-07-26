package com.example.zippick.ui.screen

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.zippick.R
import com.example.zippick.network.FakeAiLayoutApi
import com.example.zippick.ui.composable.BottomBar
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.model.dummy.AiCombineRequest
import kotlinx.coroutines.launch

@Composable
fun AiCombinedScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var resultImageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // uploadImage 함수
    fun uploadImage(uri: Uri) {
        isLoading = true
        coroutineScope.launch {
            val request = AiCombineRequest(
                furnitureImageUrl = "https://example.com/images/chair.png",
                category = "의자"
            )
            val response = FakeAiLayoutApi.postComposeRequest(uri, request)
            resultImageUrl = response.resultImageUrl
            isLoading = false
        }
    }

    // 카메라 촬영용 URI 생성
    fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DISPLAY_NAME, "camera_photo.jpg")
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }

    // 시스템 공유 시트 결과 처리
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: cameraImageUri
            if (uri != null) {
                selectedImageUri = uri
                uploadImage(uri)
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = R.drawable.closet,
                        contentDescription = "제품 이미지",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("리바트 뉴 테크닉 의자", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("150,000원", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("의자", color = MainBlue, fontSize = 14.sp)
                    }
                }
            }
        },
        bottomBar = { BottomBar(navController) },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MainBlue)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("AI로 가구를 배치하는 중입니다...", fontSize = 14.sp)
                        }
                    }
                }

                resultImageUrl != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(MainBlue.copy(alpha = 0.05f))
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "이런 느낌은 어떠신가요?",
                                    color = MainBlue,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            AsyncImage(
                                model = resultImageUrl,
                                contentDescription = "AI 합성 결과 이미지",
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                    .shadow(4.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("AI가 가구를 배치해봤어요", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ai_layout),
                            contentDescription = "AI 가구 배치 안내 이미지",
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "인공 지능을 활용하여 가구가 조화롭게\n배치된 모습을 볼 수 있어요.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 시스템 공유 시트 띄우기 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp, start = 20.dp, end = 20.dp)
            ) {
                Button(
                    onClick = {
                        val imageUri = createImageUri(context)
                        cameraImageUri = imageUri

                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        }

                        val galleryIntent = Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                        }

                        val chooser = Intent.createChooser(galleryIntent, "사진 업로드")
                        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                        launcher.launch(chooser)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "방 안의 사진을 업로드 해주세요",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

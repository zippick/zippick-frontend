package com.example.zippick.ui.screen

import android.app.Activity
import android.content.Intent
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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri.value = it
        }
    }

    fun launchGallery() {
        galleryLauncher.launch("image/*")
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
                onPickImageClick = { launchGallery() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnalyzeButton(
                label = "분석하기",
                onClick = {
                val imageUriEncoded = Uri.encode(selectedImageUri.value.toString())
                navController.navigate("photoAnalysis/$imageUriEncoded")
            })

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

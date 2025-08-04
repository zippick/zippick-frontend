package com.example.zippick.ui.composable.photo

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.zippick.ui.theme.MainBlue

@Composable
fun PhotoUploadSection(
    selectedImageUri: Uri?,
    onPickImageClick: () -> Unit
) {
    if (selectedImageUri == null) {
        Button(
            onClick = onPickImageClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MainBlue
            ),
            border = BorderStroke(1.5.dp, MainBlue),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(
                text = "+ 사진 업로드",
                color = MainBlue
            )
        }
    } else {
        Image(
            painter = rememberAsyncImagePainter(model = selectedImageUri),
            contentDescription = "선택한 이미지",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onPickImageClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MainBlue
            ),
            border = BorderStroke(1.8.dp, MainBlue),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(
                text = "다시 선택하기",
                color = MainBlue
            )
        }
    }
}

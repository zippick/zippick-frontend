package com.example.zippick.ui.composable.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.zippick.ui.model.Product
import com.example.zippick.util.LikedPreferences

@Composable
fun ProductItem(product: Product) {
    val context = LocalContext.current

    // 좋아요 상태를 저장하는 상태 변수. 초기 값은 SharedPreferences 에서 가져옴
    var liked by remember {
        mutableStateOf(LikedPreferences.isLiked(context, product.id))
    }

    Column(
        modifier = Modifier.fillMaxWidth() // 아이템 전체 너비 사용
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 정사각형 비율 유지
        ) {
            // 제품 이미지 표시
            Image(
                painter = painterResource(id = product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 찜 버튼 (하트 아이콘)
            IconButton(
                onClick = {
                    // 상태 변경 (UI 토글 + 로컬 저장소에 상태 저장)
                    liked = !liked
                    LikedPreferences.toggleLiked(context, product.id)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 오른쪽 아래에 위치
                    .padding(8.dp)
                    .border(2.dp, Color.LightGray, shape = CircleShape)
                    .background(Color.White, shape = CircleShape) // 흰 배경 + 원형
            ) {
                Icon(
                    imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "찜", // 접근성 용도
                    tint = if (liked) Color.Red else Color.Gray // 색상 변경
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // 간격 추가

        // 제품 이름 텍스트
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1 // 한 줄까지만 표시
        )

        Spacer(modifier = Modifier.height(8.dp)) // 간격 추가

        // 제품 가격 텍스트
        Text(
            text = "${product.price}원",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        // 구분선
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

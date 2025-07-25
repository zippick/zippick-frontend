package com.example.zippick.ui.composable.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.ui.model.Product
import com.example.zippick.ui.theme.HeartRed
import com.example.zippick.util.LikedPreferences

@Composable
fun ProductItem(
    product: Product,
    navController: NavController
) {
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
                .clickable {
                    navController.navigate("detail/${product.id}")
                }
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
                    .border(1.5.dp, Color.LightGray, shape = CircleShape)
                    .background(Color.White, shape = CircleShape) // 흰 배경 + 원형
                    .width(35.dp)
                    .height(35.dp)
            ) {
                Icon(
                    imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "찜", // 접근성 용도
                    tint = if (liked) HeartRed else Color.Gray // 색상 변경
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // 간격 추가

        // 제품 이름 텍스트
        Text(
            text = product.name,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // 간격 추가

        // 제품 가격 텍스트
        Text(
            text = "%,d원".format(product.price),
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        )

        // 구분선
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

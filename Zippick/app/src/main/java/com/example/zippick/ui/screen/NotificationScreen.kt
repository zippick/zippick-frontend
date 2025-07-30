import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.ui.composable.RequireLogin
import com.example.zippick.ui.composable.photo.LottieLoading
import com.example.zippick.ui.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    RequireLogin(navController = navController as NavHostController) {

        val notifications by viewModel.notifications.collectAsState()
        val loading by viewModel.loading.collectAsState()
        val context = LocalContext.current

        val listState = rememberLazyListState()

        // 최초 1회만 로딩
        LaunchedEffect(Unit) {
            viewModel.loadNotificationsAll()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(horizontal = 12.dp)
        ) {
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieLoading(modifier = Modifier.size(120.dp))
                    }
                }
            }
            // 중복 아이템 제거 + 안정적인 키 생성을 위한 표시용 리스트
            val displayList = remember(notifications) {
                notifications
                    .distinctBy { it.id to (it.createdAt ?: "") }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState
            ) {
                items(displayList, key = { n -> "notif-${n.id}-${n.createdAt ?: ""}" }) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(
                                color = Color(0xFFEAEAEA),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_alarm_success),
                                        contentDescription = "체크 아이콘",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 6.dp)
                                    )
                                    Text(
                                        text = item.title ?: "",
                                        fontWeight = FontWeight(500),
                                        fontSize = 16.sp
                                    )
                                }

                                Text(
                                    text = formatToRelativeTime(item.createdAt),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.body ?: "상품 결제가 완료되었습니다.",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatToRelativeTime(input: String?): String {
    if (input.isNullOrBlank()) return ""

    return try {
        val now = java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Seoul"))

        val parsed = try {
            java.time.OffsetDateTime.parse(input).atZoneSameInstant(java.time.ZoneId.of("Asia/Seoul"))
        } catch (e: Exception) {
            java.time.LocalDateTime.parse(input).atZone(java.time.ZoneId.of("Asia/Seoul"))
        }

        val duration = java.time.Duration.between(parsed, now)

        when {
            duration.toMinutes() < 1 -> "방금 전"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
            duration.toHours() < 24 -> "${duration.toHours()}시간 전"
            duration.toDays() == 1L -> "어제"
            duration.toDays() <= 7L -> "${duration.toDays()}일 전"
            else -> java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd").format(parsed)
        }
    } catch (e: Exception) {
        ""
    }
}

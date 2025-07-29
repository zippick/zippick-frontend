import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zippick.ui.viewmodel.NotificationViewModel
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current

    var offset by remember { mutableStateOf(0) }
    var isEnd by remember { mutableStateOf(false) }
    var isPaging by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // 최초 1회만 로딩
    LaunchedEffect(Unit) {
        offset = 0
        isEnd = false
        isPaging = true
        viewModel.loadNotifications(offset = 0, append = false) { list ->
            isEnd = list.isEmpty()
            isPaging = false
        }
    }

    // 무한스크롤 트리거 (맨 아래 2개 도달 시마다)
    LaunchedEffect(listState, notifications) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisible
        }.collectLatest { lastVisibleIndex ->
            if (
                !isEnd && !isPaging &&
                lastVisibleIndex >= notifications.size - 2 && // 마지막 2개 이내 접근 시
                notifications.isNotEmpty()
            ) {
                isPaging = true
                val nextOffset = notifications.size // 누적 데이터 크기로 페이지 기준 이동
                viewModel.loadNotifications(offset = nextOffset, append = true) { list ->
                    if (list.isEmpty()) {
                        isEnd = true
                    }
                    isPaging = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        if (loading && !isPaging) {
            Text(
                "로딩 중...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✓",
                                fontSize = 20.sp,
                                color = Color(0xFF222222),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = item.title ?: "",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = (formatToKST(item.createdAt)).let { if(it.isNotEmpty()) " · $it" else "" },
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.body ?: "",
                            fontSize = 15.sp,
                            color = Color(0xFF222222)
                        )
                    }
                }
            }
        }
        // 맨 아래에 페이징 중 안내
        if (isPaging) {
            Text(
                text = "불러오는 중...",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp),
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatToKST(input: String?): String {
    if (input.isNullOrBlank()) return ""
    return try {
        // 1. OffsetDateTime(ISO 8601 with Z or +09:00) 파싱 시도
        val zdt = java.time.OffsetDateTime.parse(input)
        val kst = zdt.atZoneSameInstant(java.time.ZoneId.of("Asia/Seoul"))
        java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(kst)
    } catch (e: Exception) {
        try {
            // 2. LocalDateTime(yyyy-MM-ddTHH:mm:ss)로 시도, KST로 변환
            val ldt = java.time.LocalDateTime.parse(input)
            val kst = ldt.atZone(java.time.ZoneId.of("Asia/Seoul"))
            java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(kst)
        } catch (e: Exception) {
            ""
        }
    }
}
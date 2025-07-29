package com.example.zippick.ui.screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zippick.R
import com.example.zippick.network.notification.NotificationSendRequest
import com.example.zippick.ui.model.OrderRequest
import com.example.zippick.ui.viewmodel.NotificationViewModel
import com.example.zippick.ui.viewmodel.OrderViewModel
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.tosspayments.paymentsdk.view.Agreement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/* 결제 수단 렌더링 페이지 */
class PaymentMethodActivity : AppCompatActivity() {
    private val clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"
    private lateinit var widget: PaymentWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        // PaymentComposeActivity에서 전달받은 데이터
        val productId = intent.getLongExtra("productId",  0)
        val productName = intent.getStringExtra("productName") ?: ""
        val productImage = intent.getStringExtra("productImage") ?: ""
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productAmount = intent.getIntExtra("productAmount", 1)

        super.onCreate(savedInstanceState)
        widget = PaymentWidget(this, clientKey, UUID.randomUUID().toString())

        setContent {
            PaymentMethodScreen(widget, productId, productName, productImage, productPrice, productAmount)
        }
    }
}

@Composable
fun PaymentMethodScreen(
    widget: PaymentWidget,
    productId: Long,
    productName: String,
    productImage: String,
    productPrice: Int,
    productAmount: Int
) {
    val context = LocalContext.current
    val orderId = "order_${System.currentTimeMillis()}"
    val totalPrice = productPrice * productAmount
    val orderViewModel: OrderViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    val orderResult by orderViewModel.orderResult.collectAsState()
    LaunchedEffect(orderResult) {
        orderResult?.let { result ->
            orderViewModel.clearOrderResult()
            if (result == "주문이 정상적으로 완료되었습니다.") {
                val now = System.currentTimeMillis()
                val sdf = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.getDefault())
                val orderDate = sdf.format(Date(now))

                val intent = Intent(context, OrderCompleteActivity::class.java)
                intent.putExtra("orderNumber", orderId)
                intent.putExtra("orderDate", orderDate)
                intent.putExtra("productName", productName)
                intent.putExtra("productImage", productImage)
                intent.putExtra("productPrice", productPrice)
                intent.putExtra("productAmount", productAmount)
                intent.putExtra("totalPrice", totalPrice)
                context.startActivity(intent)
            } else {
                // 실패 시 화면 이동 없이 Toast로만 안내
                Toast.makeText(context, "주문 실패: $result", Toast.LENGTH_LONG).show()
            }
        }
    }
    Scaffold(
        topBar = {
            IconButton(onClick = {
                // 액티비티 종료 = 뒤로가기
                (context as? AppCompatActivity)?.finish()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // 뒤로가기 버튼
                    contentDescription = "뒤로가기"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AndroidView(
                factory = { ctx -> PaymentMethod(ctx) },
                update = { view ->
                    widget.renderPaymentMethods(
                        method = view,
                        amount = PaymentMethod.Rendering.Amount(totalPrice),
                        options = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            AndroidView(
                factory = { ctx -> Agreement(ctx) },
                update = { view -> widget.renderAgreement(view) },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    widget.requestPayment(
                        paymentInfo = PaymentMethod.PaymentInfo(
                            orderId = orderId,
                            orderName = productName
                        ),
                        paymentCallback = object : PaymentCallback {
                            override fun onPaymentSuccess(success: TossPaymentResult.Success) {
                                Toast.makeText(context, "결제 성공: ${success.paymentKey}", Toast.LENGTH_SHORT).show()
                                val request = OrderRequest(
                                    totalPrice = productPrice * productAmount,
                                    count = productAmount,
                                    merchantOrderId = success.paymentKey,
                                    productId = productId
                                )
                                orderViewModel.postOrder(request)
                                val notificationRequest = NotificationSendRequest(
                                    title = "주문이 완료되었습니다!",
                                    content = "주문번호 ${success.paymentKey}이(가) 정상 처리되었습니다.",
                                    createdAt = SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss",
                                        Locale.getDefault()
                                    ).format(Date())
                                )
                                notificationViewModel.sendNotification(notificationRequest)
                            }
                            override fun onPaymentFailed(fail: TossPaymentResult.Fail) {
                                // 실패시 화면 이동 없이 Toast만 표시
                                Toast.makeText(context, "결제 실패: ${fail.errorMessage ?: "알 수 없는 오류"}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("결제하기")
            }
        }
    }
}
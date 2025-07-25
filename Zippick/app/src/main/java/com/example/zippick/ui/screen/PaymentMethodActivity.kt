package com.example.zippick.ui.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.tosspayments.paymentsdk.view.Agreement
import java.util.UUID

/* 결제 수단 렌더링 페이지 */
class PaymentMethodActivity : AppCompatActivity() {
    private val clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"
    private lateinit var widget: PaymentWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        // PaymentComposeActivity에서 전달받은 데이터
        val orderName = intent.getStringExtra("orderName") ?: ""
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productAmount = intent.getIntExtra("productAmount", 1)

        super.onCreate(savedInstanceState)
        widget = PaymentWidget(this, clientKey, UUID.randomUUID().toString())

        setContent {
            PaymentMethodScreen(widget, orderName, productPrice, productAmount)
        }
    }
}

@Composable
fun PaymentMethodScreen(widget: PaymentWidget, orderName: String, productPrice: Int, productAmount: Int) {
    val context = LocalContext.current
    val orderId = "order_${System.currentTimeMillis()}"
    val amount = productPrice * productAmount // 총가격

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 결제수단 선택 뷰
        AndroidView(
            factory = { ctx -> PaymentMethod(ctx) },
            update = { view ->
                widget.renderPaymentMethods(
                    method = view,
                    amount = PaymentMethod.Rendering.Amount(amount),
                    options = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // 약관 동의 뷰
        AndroidView(
            factory = { ctx -> Agreement(ctx) },
            update = { view ->
                widget.renderAgreement(view)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                widget.requestPayment(
                    paymentInfo = PaymentMethod.PaymentInfo(
                        orderId = orderId,
                        orderName = orderName
                    ),
                    paymentCallback = object : PaymentCallback {
                        override fun onPaymentSuccess(success: TossPaymentResult.Success) {
                            Toast.makeText(context, "결제 성공: ${success.paymentKey}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onPaymentFailed(fail: TossPaymentResult.Fail) {
                            Toast.makeText(context, "결제 실패: ${fail.errorMessage}", Toast.LENGTH_SHORT).show()
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
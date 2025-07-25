package com.example.zippick.ui.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.tosspayments.paymentsdk.view.Agreement
import java.util.UUID

class PaymentComposeActivity : AppCompatActivity() {
    private val clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"
    private lateinit var widget: PaymentWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widget = PaymentWidget(this, clientKey, UUID.randomUUID().toString())

        // 1) JS 기반 결제 위젯 초기 로드 (오프스크린)
        val methodView = PaymentMethod(this).also {
            widget.renderPaymentMethods(
                method = it,
                amount = PaymentMethod.Rendering.Amount(10000),
                options = null
            )
        }
        val agreementView = Agreement(this).also {
            widget.renderAgreement(
                it,
                paymentWidgetStatusListener = null
            )
        }

        // 2) Compose UI 세팅
        setContent {
            PaymentScreen(widget)
        }
    }
}

@Composable
fun PaymentScreen(widget: PaymentWidget) {
    val context = LocalContext.current

    var orderId by remember { mutableStateOf("order_${System.currentTimeMillis()}") }
    var orderName by remember { mutableStateOf("Android Kotlin 테스트") }
    var amount by remember { mutableStateOf("10000") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 주문 정보 입력
        OutlinedTextField(
            value = orderId,
            onValueChange = { orderId = it },
            label = { Text("주문 번호") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = orderName,
            onValueChange = { orderName = it },
            label = { Text("상품명") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it.filter { ch -> ch.isDigit() } },
            label = { Text("금액") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 결제 요청 버튼 (항상 활성화)
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
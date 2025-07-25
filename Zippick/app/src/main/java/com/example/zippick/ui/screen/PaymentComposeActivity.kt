package com.example.zippick.ui.screen

import android.content.Intent
import android.os.Bundle
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
import com.tosspayments.paymentsdk.view.PaymentMethod
import com.tosspayments.paymentsdk.view.Agreement
import java.util.UUID

class PaymentComposeActivity : AppCompatActivity() {
    private val clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm" // 토스 클라이언트 키
    private lateinit var widget: PaymentWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widget = PaymentWidget(this, clientKey, UUID.randomUUID().toString())

        // 1) 결제수단 선택 뷰 생성
        val methodView = PaymentMethod(this).also {
            widget.renderPaymentMethods(
                method = it,
                amount = PaymentMethod.Rendering.Amount(10000), // 결제금액
                options = null
            )
        }
        // 1-1) 약관 뷰 생성
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
        Button(onClick = {
            val intent = Intent(context, PaymentMethodActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("결제수단 선택으로 이동")
        }
    }
}
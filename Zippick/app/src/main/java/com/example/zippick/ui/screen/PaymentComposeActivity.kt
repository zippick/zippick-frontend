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
            // 결제 수단 위젯 초기화
            widget.renderPaymentMethods(
                method = it,
                amount = PaymentMethod.Rendering.Amount(0),
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

/* [결제하기] 버튼 렌더링 페이지 */
@Composable
fun PaymentScreen(widget: PaymentWidget) {
    val context = LocalContext.current
    // 1. 상품명, 가격 변수 선언 (고정)
    val productName = "테스트상품명"
    val productPrice = 2000

    // 2. 수량 입력값 상태로 관리
    var productAmount by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 3. 상품명, 가격 출력
        Text("상품명 : $productName")
        Text("가격 : $productPrice")

        // 4. 수량 입력창 (숫자만 입력 가능)
        OutlinedTextField(
            value = productAmount,
            onValueChange = { productAmount = it },
            label = { Text("수량") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // 결제 요청 버튼
        Button(onClick = {
            val intent = Intent(context, PaymentMethodActivity::class.java).apply {
                putExtra("productName", productName)
                putExtra("productPrice", productPrice)
                putExtra("productAmount", productAmount.toIntOrNull() ?: 1)
            }
            context.startActivity(intent) // 화면 이동 시 intent 같이 보냄
        }) {
            Text("결제하기")
        }
    }
}
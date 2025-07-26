package com.example.zippick.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.Typography

@Preview
@Composable
fun LoginScreen(
    onLoginClick: (String, String, (Boolean) -> Unit) -> Unit = { _, _, _ -> },
    onSignUpClick: () -> Unit = {}
) {
    var userId by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 아이디 + 비밀번호 입력 박스
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 6.dp,
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 아이디 입력
                    TextField(
                        value = userId,
                        onValueChange = {
                            userId = it
                            errorMessage = null
                        },
                        placeholder = { Text("아이디", style = Typography.bodyLarge) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = noLineTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)

                    // 비밀번호 입력
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        placeholder = { Text("비밀번호", style = Typography.bodyLarge) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = noLineTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 🔹 에러 메시지 표시 영역
        if (errorMessage != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MainBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = errorMessage!!,
                    color = MainBlue,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 로그인 버튼
        Button(
            onClick = {
                // 로그인 요청 후 콜백으로 성공 여부 판단
                onLoginClick(userId.text, password.text) { success ->
                    errorMessage = if (success) null else "아이디 또는 비밀번호를 확인해주세요."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
        ) {
            Text("로그인", style = Typography.titleLarge, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "회원가입",
            style = Typography.bodyLarge.copy(fontSize = 14.sp),
            color = MainBlue,
            modifier = Modifier.clickable { onSignUpClick() }
        )
    }
}

// 🔹 밑줄 제거 TextField 스타일
@Composable
fun noLineTextFieldColors() = TextFieldDefaults.colors(
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent
)

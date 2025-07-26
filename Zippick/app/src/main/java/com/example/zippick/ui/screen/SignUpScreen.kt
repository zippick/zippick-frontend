package com.example.zippick.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.DarkGray
import com.example.zippick.ui.theme.Typography

@Preview(showBackground = true)
@Composable
fun SignUpScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var basicAddress by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = DarkGray,
        unfocusedTextColor = DarkGray,
        focusedPlaceholderColor = DarkGray,
        unfocusedPlaceholderColor = DarkGray,
        focusedBorderColor = DarkGray,
        unfocusedBorderColor = DarkGray,
        cursorColor = DarkGray
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 이메일
            Column {
                Text(text = "이메일", style = Typography.bodyLarge.copy(color = DarkGray))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("이메일 입력", style = Typography.bodyLarge.copy(color = DarkGray)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors,
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Button(
                                onClick = { /* 중복 확인 로직 */ },
                                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    "중복확인",
                                    color = Color.White,
                                    style = Typography.bodyLarge.copy(fontSize = 12.sp)
                                )
                            }
                        }
                    }
                )
            }

            // 비밀번호
            Column {
                Text(text = "비밀번호", style = Typography.bodyLarge.copy(color = DarkGray))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("비밀번호 입력", style = Typography.bodyLarge.copy(color = DarkGray)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            // 비밀번호 확인
            Column {
                Text(text = "비밀번호 확인", style = Typography.bodyLarge.copy(color = DarkGray))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it },
                    placeholder = { Text("비밀번호 확인 입력", style = Typography.bodyLarge.copy(color = DarkGray)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            // 이름
            Column {
                Text(text = "이름", style = Typography.bodyLarge.copy(color = DarkGray))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("이름 입력", style = Typography.bodyLarge.copy(color = DarkGray)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            // 주소
            Column {
                Text(text = "주소", style = Typography.bodyLarge.copy(color = DarkGray))
                Spacer(Modifier.height(8.dp))

                // 우편번호 + 주소검색 버튼
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* 주소 검색 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .height(56.dp)
                    ) {
                        Text("주소검색", color = Color.White, fontSize = 16.sp, style = Typography.bodyLarge)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 기본 주소 입력
                OutlinedTextField(
                    value = basicAddress,
                    onValueChange = { basicAddress = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )

                Spacer(Modifier.height(8.dp))

                // 상세 주소 입력
                OutlinedTextField(
                    value = detailAddress,
                    onValueChange = { detailAddress = it },
                    placeholder = { Text("상세 주소 입력", style = Typography.bodyLarge.copy(color = DarkGray)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            // 가입하기 버튼
            Button(
                onClick = { /* 가입 처리 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text("가입하기", color = Color.White, fontSize = 18.sp, style = Typography.bodyLarge)
            }
        }
    }
}

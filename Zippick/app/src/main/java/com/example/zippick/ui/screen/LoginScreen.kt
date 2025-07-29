package com.example.zippick.ui.screen

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.TokenManager
import com.example.zippick.network.auth.AuthService
import com.example.zippick.ui.model.LoginRequest
import com.example.zippick.ui.theme.DarkGray
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 6.dp,
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = userId,
                        onValueChange = {
                            userId = it
                            errorMessage = null
                        },
                        placeholder = { Text("아이디", style = Typography.bodyLarge) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = noLineTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)

                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        placeholder = { Text("비밀번호", style = Typography.bodyLarge) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = noLineTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

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
                coroutineScope.launch {
                    try {
                        val request = LoginRequest(userId.text, password.text)
                        val response = RetrofitInstance.retrofit
                            .create(AuthService::class.java)
                            .login(request)

                        TokenManager.saveToken(response.token)
                        errorMessage = null

                        // ✅ 로그인 성공 시 home으로 이동
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("LoginError", "예외 발생: ${e.message}", e)
                        errorMessage = "아이디 또는 비밀번호를 확인해주세요."
                    }
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
            modifier = Modifier.clickable {
                navController.navigate("signup")
            }
        )
    }
}

@Composable
fun noLineTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = DarkGray,
    unfocusedTextColor = DarkGray,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    cursorColor = DarkGray,
    focusedPlaceholderColor = DarkGray,
    unfocusedPlaceholderColor = DarkGray
)

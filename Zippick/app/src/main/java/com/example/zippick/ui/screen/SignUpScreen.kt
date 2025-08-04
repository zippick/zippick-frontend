package com.example.zippick.ui.screen

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.WebViewActivity
import com.example.zippick.network.RetrofitInstance
import com.example.zippick.network.member.MemberService
import com.example.zippick.ui.model.SignUpRequest
import com.example.zippick.ui.theme.DarkGray
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.Typography
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var isEmailChecked by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var basicAddress by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val addressLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            address = result.data?.getStringExtra("zipcode") ?: ""
            basicAddress = (result.data?.getStringExtra("basicAddress") ?: "") +
                    (result.data?.getStringExtra("extraAddress") ?: "")
        }
    }

    val isFormValid = email.isNotBlank() &&
            password.isNotBlank() &&
            password == passwordConfirm &&
            name.isNotBlank() &&
            address.isNotBlank() &&
            basicAddress.isNotBlank() &&
            detailAddress.isNotBlank() &&
            isEmailChecked

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = DarkGray,
        unfocusedTextColor = DarkGray,
        focusedPlaceholderColor = DarkGray,
        unfocusedPlaceholderColor = DarkGray,
        focusedBorderColor = MainBlue,
        unfocusedBorderColor = DarkGray,
        cursorColor = DarkGray
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text("이메일", fontWeight = FontWeight(500), color = DarkGray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailChecked = false
                        emailError = null
                    },
                    placeholder = { Text("이메일 입력", style = Typography.bodyLarge) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors,
                    trailingIcon = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val response = RetrofitInstance.retrofit.create(MemberService::class.java).checkEmail(email)
                                        if (response.isSuccessful) {
                                            val isDuplicated = response.body() ?: false
                                            if (isDuplicated) {
                                                emailError = "이미 사용 중인 이메일입니다."
                                            } else {
                                                emailError = null
                                                isEmailChecked = true
                                            }
                                        } else {
                                            emailError = "서버 오류 발생"
                                        }
                                    } catch (_: Exception) {
                                        emailError = "오류 발생"
                                    }
                                }
                            },
                            enabled = email.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            modifier = Modifier.padding(end = 8.dp)

                        ) {
                            Text("중복확인", color = Color.White, fontSize = 12.sp)
                        }
                    }
                )
                if (emailError != null) {
                    Text(emailError!!, color = Color.Red, fontSize = 12.sp)
                } else if (isEmailChecked) {
                    Text("사용 가능한 이메일입니다.", color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }

            Column {
                Text("비밀번호", fontWeight = FontWeight(500), color = DarkGray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    placeholder = { Text("비밀번호 입력", style = Typography.bodyLarge) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            Column {
                Text("비밀번호 확인", fontWeight = FontWeight(500), color = DarkGray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordConfirm,
                    onValueChange = {
                        passwordConfirm = it
                        passwordError = if (password != it) "비밀번호가 일치하지 않습니다." else null
                    },
                    placeholder = { Text("비밀번호 확인 입력", style = Typography.bodyLarge) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
                if (passwordError != null) {
                    Text(passwordError!!, color = Color.Red, fontSize = 12.sp)
                } else if (passwordConfirm.isNotBlank() && password == passwordConfirm) {
                    Text("비밀번호가 일치합니다.", color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }
            Column {
                Text("이름", fontWeight = FontWeight(500), color = DarkGray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("이름", style = Typography.bodyLarge) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            Text("주소", fontWeight = FontWeight(500), color = DarkGray)
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = address,
                    onValueChange = {},
                    placeholder = { Text("우편번호", style = Typography.bodyLarge) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors,
                    readOnly = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(context, WebViewActivity::class.java)
                        addressLauncher.launch(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("주소검색", color = Color.White, fontSize = 16.sp)
                }
            }

            OutlinedTextField(
                value = basicAddress,
                onValueChange = { basicAddress = it },
                placeholder = { Text("기본 주소", style = Typography.bodyLarge) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = detailAddress,
                onValueChange = { detailAddress = it },
                placeholder = { Text("상세 주소", style = Typography.bodyLarge) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val request = SignUpRequest(
                                loginId = email,
                                password = password,
                                name = name,
                                zipcode = address,
                                basicAddress = basicAddress,
                                detailAddress = detailAddress
                            )
                            RetrofitInstance.retrofit.create(MemberService::class.java).signUp(request)
                            Toast.makeText(context, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        } catch (e: HttpException) {
                            Toast.makeText(context, "오류 발생: ${e.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text("가입하기", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

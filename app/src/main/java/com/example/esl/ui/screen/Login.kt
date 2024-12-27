package com.example.esl.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esl.R
import com.example.esl.models.network.LoginRequest
import com.example.esl.models.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    context: Context // Tambahkan parameter context untuk SharedPreferences
) {
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF40E0D0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoesl),
                contentDescription = "ESL Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )
            Text(
                text = "Username",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Text(
                text = "Password",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val loginRequest = LoginRequest(username = username, password = password)
                            println("Request data: $loginRequest")
                            val response = RetrofitInstance.api.login(loginRequest)
                            println("Response: $response")

                            // Simpan token ke SharedPreferences jika login berhasil
                            val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("jwt_token", response.token) // Asumsi bahwa response mengandung token
                            editor.apply() // Simpan perubahan ke SharedPreferences

                            // Panggil fungsi onLoginSuccess
                            onLoginSuccess()
                        } catch (e: Exception) {
                            errorMessage = "Login gagal: ${e.message}"
                            println("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0FFFF)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Masuk",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            // Tampilkan pesan error di sini
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
            }
            TextButton(
                onClick = { onRegisterClick() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Belum punya akun? Register di sini",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF40E0D0), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
package com.example.esl.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esl.R
import com.example.esl.models.network.RegisterRequest
import com.example.esl.models.network.RetrofitInstance
import com.example.esl.ui.theme.BackgroundColor
import com.example.esl.ui.theme.ButtonColors
import com.example.esl.ui.theme.ESLTheme
import kotlinx.coroutines.launch

@Composable
fun Register(modifier: Modifier = Modifier, navController: NavController, onRegisterSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    var nama by remember { mutableStateOf("") }
    var noHP by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val validateFields = nama.isNotBlank() && noHP.isNotBlank() && email.isNotBlank() &&
            username.isNotBlank() && password.isNotBlank()

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 40.dp)
                .background(BackgroundColor)
        ) {
            Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "DAFTAR AKUN",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.size(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp)
        ) {
            // Nama
            TextField(
                value = nama,
                onValueChange = { nama = it },
                singleLine = true,
                label = { Text("Nama") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            if (nama.isBlank()) Text("Nama tidak boleh kosong", color = Color.Red)

            // No HP
            TextField(
                value = noHP,
                onValueChange = { noHP = it },
                singleLine = true,
                label = { Text("No HP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            if (noHP.isBlank()) Text("No HP tidak boleh kosong", color = Color.Red)

            // Email
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            if (email.isBlank()) Text("Email tidak boleh kosong", color = Color.Red)

            // Username
            TextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color.White.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            if (username.isBlank()) Text("Username tidak boleh kosong", color = Color.Red)

            // Password
            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Password") },
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
            if (password.isBlank()) Text("Password tidak boleh kosong", color = Color.Red)
        }

        Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = { Log.d("RegisterButton", "Tombol Daftar diklik")
                    coroutineScope.launch {
                        Log.d("RegisterCoroutine", "Coroutine dimulai")
                        if(validateFields) {
                            isLoading = true
                            Log.d("LoadingState", "isLoading: $isLoading")
                            try {
                                Log.d("API Call", "Sending request to API")
                                val response = RetrofitInstance.api.register(
                                    RegisterRequest(nama, noHP, email, username, password)
                                )
                                Log.d("API Response", "Response: ${response.body()?.message}")

                                if (response.isSuccessful && response.body() != null) {
                                    val registerResponse = response.body()
                                    if (registerResponse?.success == true) {
                                        Log.d("RegisterSuccess", "Registrasi berhasil")
                                        onRegisterSuccess()// Notifikasi bahwa register berhasil
                                    } else {
                                        errorMessage =
                                            "Register gagal: ${registerResponse?.message ?: "Unknown Error"}"
                                        Log.e("RegisterError", errorMessage)
                                    }
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    errorMessage = "Register gagal: ${
                                        response.errorBody()?.string() ?: "Unknown Error"
                                    }"
                                    Log.e("APIError", errorMessage)
                                }
                            } catch (e: Exception) {
                                errorMessage = "Terjadi kesalahan: ${e.localizedMessage}"
                                Log.e("Exception", errorMessage, e)
                                e.printStackTrace()
                            } finally {
                                isLoading = false
                                Log.d("LoadingState", "isLoading: $isLoading")
                            }
                        } else {
                            errorMessage = "Harap Isi Semua Kolom"
                        }

                } },
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(21.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColors,
                    contentColor = Color.Black // Warna text
                ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Daftar",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.size(15.dp))

        TextButton(
            onClick = { onLoginClick() },
            modifier = Modifier
                .padding(top = 16.dp)
                .background(Color.Transparent, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Sudah punya akun? Masuk di sini",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF40E0D0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}




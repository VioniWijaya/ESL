package com.example.esl.models.network

import com.example.esl.models.local.entities.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val BASE_URL = "http://192.168.1.12:3000/api/auth/"

data class RegisterRequest(
    val nama: String,
    val no_Hp: String,
    val email: String,
    val username: String,
    val password: String,
//    val foto_profil: String
)

data class AuthResponse(
    val token: String,
    val user: User
)
data class UserResponse(
    val id: Int,
    val nama: String,
    val no_HP: String,
    val email: String,
    val username: String,
    val foto_profil: String?,
    val created_at: String,
    val updated_at: String
)

data class LoginRequest(val email: String, val password: String)

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}


object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

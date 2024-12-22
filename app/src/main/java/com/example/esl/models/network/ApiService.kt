package com.example.esl.models.network

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.local.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val BASE_URL = "http://192.168.19.66:3000/"

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
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: User?
)

data class LoginRequest(val username: String, val password: String)

data class RentalHistory(
    val status: String,
    val name: String,
    val date: String,
    val owner: String
)

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
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


// Retrofit API Interface
interface RentalApi {
    @GET("rental_history") // Endpoint API
    suspend fun getRentalHistory(): List<RentalHistory>
}
class RentalViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://yourapiurl.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(RentalApi::class.java)

    var rentalData by mutableStateOf<List<RentalHistory>>(emptyList())
        private set

    init {
        fetchRentalHistory()
    }

    private fun fetchRentalHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getRentalHistory()
                // Filter data dengan status "Selesai"
                rentalData = data.filter { it.status == "Selesai" }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
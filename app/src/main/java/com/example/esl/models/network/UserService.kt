package com.example.esl.models.network

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- Model Data ---
data class User(
    val id: Int,
    val username: String,
    val email: String
)

// --- Retrofit Service ---
interface UserApi {
    @GET("api/profile")
    suspend fun getUserProfile(): User
}

// --- User Service Singleton ---
object UserService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.54.66:3000/") // Ganti dengan base URL Anda
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: UserApi = retrofit.create(UserApi::class.java)
}

// --- ViewModel ---
class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow(User(0, "", ""))
    val user: StateFlow<User> get() = _user

    val isLoading = mutableStateOf(true)

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val fetchedUser = UserService.api.getUserProfile()
                _user.value = fetchedUser
            } catch (e: Exception) {
                e.printStackTrace() // Handle error here (e.g., show a Toast)
            } finally {
                isLoading.value = false
            }
        }
    }
}

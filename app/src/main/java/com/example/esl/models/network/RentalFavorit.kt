package com.example.esl.models.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

data class RentalFavorite(
    val id_favorit: Int,
    val id_properti: Int,
    val nama_properti: String,
    val hargaSewa: Double,
    val lokasi: String,
    val tanggal_ditambahkan: String
)

interface RentalFavoriteApi {
    @GET("api/favorite")
    suspend fun getFavorites(@Header("Authorization") token: String): Response<List<RentalFavorite>>

    companion object {
        private const val BASE_URL = "http://192.168.54.66:3000/"

        fun create(): RentalFavoriteApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RentalFavoriteApi::class.java)
        }
    }
}

class RentalFavoriteViewModel : ViewModel() {

    private val apiService = RentalFavoriteApi.create()

    private val _favoriteData = MutableStateFlow<List<RentalFavorite>>(emptyList())
    val favoriteData: StateFlow<List<RentalFavorite>> get() = _favoriteData

    // Fungsi untuk memuat data favorit dari API dengan token
    fun loadFavorites(token: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<RentalFavorite>> = apiService.getFavorites(token)

                if (response.isSuccessful) {
                    Log.d("RentalFavoriteViewModel", "API Response: ${response.body()}")
                    _favoriteData.value = response.body() ?: emptyList()
                } else {
                    Log.e("RentalFavoriteViewModel", "API call failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RentalFavoriteViewModel", "Error loading favorites: ${e.message}")
            }
        }
    }
}

package com.example.esl.models.network


import android.icu.util.TimeUnit
import com.example.esl.models.local.entities.Property

import okhttp3.OkHttpClient
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import retrofit2.Call
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esl.models.local.entities.Penyewaan
import com.example.esl.models.local.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

const val BASE_URL = "http://192.168.1.14:3000/"

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


data class PropertyListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Property>
)

data class PropertyResponse(
    val success: Boolean,
    val message: String,
    val data: Property
)

data class UlasanRequest(
    val id_users: Int,
    val id_penyewaan: Int,
    val ulasan: String,
    val rating: Int,
    val media_ulasan: String? = null
)

data class UlasanResponse(
    val id_ulasan: Int,
    val id_users: Int,
    val id_penyewaan: Int,
    val ulasan: String,
    val rating: Int,
    val media_ulasan: String?,
    val tanggal_input: String
)

data class PenyewaanResponse(
    val id_penyewaan: Int,
    val id_users: Int,
    val id_properti: Int,
    val tanggalMulai: String,
    val tanggalAkhir: String,
    val tanggalOrder: String,
    val masaSewa: Int,
    val status: String,
    val properti: PropertyDetail,
    val user: UserResponse
)

data class PropertyDetail(
    val id_properti: Int,
    val nama_properti: String,
    val jenis_properti: String,
    val hargaSewa: Int,
    val lokasi: String
)

data class PenyewaanRequest(
    val id_users: Int,
    val id_properti: Int,
    val tanggalMulai: String,
    val tanggalAkhir: String
)


interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    // Property Endpoints
    @GET("api/properti")
    suspend fun getAllProperties(): Response<PropertyListResponse>

    @GET("api/properti/{id}")
    suspend fun getDetailProperti(@Path("id") id: Int): Response<PropertyResponse>

    // Penyewaan Endpoints
    @GET("api/penyewaan/user/{id_users}")
    suspend fun getPenyewaanByUser(@Path("id_users") idUsers: Int): Response<List<PenyewaanResponse>>

    @POST("api/penyewaan")
    suspend fun createPenyewaan(@Body penyewaanRequest: PenyewaanRequest): Response<PenyewaanResponse>

    @POST("api/ulasan")
    suspend fun createUlasan(@Body ulasanRequest: UlasanRequest): Response<UlasanResponse>

//    @PUT("api/ulasan/{id}")
//    suspend fun updateUlasan(
//        @Path("id") id: Int,
//        @Body ulasanRequest: UlasanRequest
//    ): Response<UlasanResponse>

//    @Multipart
//    @POST("reviews")
//    suspend fun addReview(
//        @Header("Authorization") token: String,
//        @Part("id_penyewaan") idPenyewaan: Int,
//        @Part("ulasan") ulasan: String,
//        @Part("rating") rating: Int,
//        @Part mediaPart: MultipartBody.Part?
//    ): Response<Void>

    @GET("api/ulasan/penyewaan/{id_penyewaan}")
    suspend fun getUlasanByPenyewaan(@Path("id_penyewaan") idPenyewaan: Int): Response<List<UlasanResponse>>
}

object RetrofitInstance {

//    private val client = OkHttpClient.Builder()
//        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
//        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
//        .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
//        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
// .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}



//package com.example.esl.models.network
//
//import android.content.Context
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.io.File
//
//object ReviewService {
//    suspend fun addReview(
//        review: UlasanRequest,
//        token: String,
//        context: Context
//    ) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.example.com/") // Ganti dengan URL server Anda
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val api = retrofit.create(ApiService::class.java)
//
//        val mediaPart = review.media_ulasan?.let {
//            val file = File(it)
//            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//            MultipartBody.Part.createFormData("media_ulasan", file.name, requestFile)
//        }
//
//        val response = api.addReview(
//            token = "Bearer $token",
//            idPenyewaan = review.id_penyewaan,
//            ulasan = review.ulasan,
//            rating = review.rating,
//            mediaPart = mediaPart
//        )
//
//        if (!response.isSuccessful) {
//            throw Exception("Gagal mengirim ulasan: ${response.message()}")
//        }
//    }
//}

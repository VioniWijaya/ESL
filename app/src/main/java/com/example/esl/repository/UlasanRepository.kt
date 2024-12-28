//package com.example.esl.repository
//
//import com.example.esl.models.network.ApiService
//import com.example.esl.models.network.RetrofitInstance
//import com.example.esl.models.network.UlasanRequest
//import com.example.esl.models.network.UlasanResponse
//
//class UlasanRepository(private val api: ApiService) {
//    suspend fun createUlasan(ulasanRequest: UlasanRequest): Result<UlasanResponse> {
//        return try {
//            val response = api.createUlasan(ulasanRequest)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("Response body is null"))
//            } else {
//                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
//                Result.failure(Exception(errorMessage))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun updateUlasan(id: Int, ulasanRequest: UlasanRequest): Result<UlasanResponse> {
//        return try {
//            val response = api.updateUlasan(id, ulasanRequest)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("Response body is null"))
//            } else {
//                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
//                Result.failure(Exception(errorMessage))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}

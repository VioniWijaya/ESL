package com.example.esl.repository

import com.example.esl.models.network.RetrofitInstance
import com.example.esl.models.network.UlasanRequest
import com.example.esl.models.network.UlasanResponse

class UlasanRepository {
    private val api = RetrofitInstance.api

    suspend fun createUlasan(ulasanRequest: UlasanRequest): Result<UlasanResponse> {
        return try {
            val response = api.createUlasan(ulasanRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUlasan(id: Int, ulasanRequest: UlasanRequest): Result<UlasanResponse> {
        return try {
            val response = api.updateUlasan(id, ulasanRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

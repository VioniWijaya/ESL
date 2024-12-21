package com.example.esl.models.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.esl.models.local.relations.UserWithPenyewaan

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE id_users = :userId")
    suspend fun getUserWithPenyewaan(userId: Int): List<UserWithPenyewaan>
}

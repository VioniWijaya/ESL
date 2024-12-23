package com.example.esl.models.local.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface HistoryDao {
    @Entity(tableName = "rental_history")
    data class RentalHistoryEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val status: String,
        val name: String,
        val date: String,
        val owner: String,
        val statusColor: Int
    )

    @Dao
    interface RentalHistoryDao {
        @Query("SELECT * FROM rental_history")
        fun getAllRentalHistory(): Flow<List<RentalHistoryEntity>>
    }

}
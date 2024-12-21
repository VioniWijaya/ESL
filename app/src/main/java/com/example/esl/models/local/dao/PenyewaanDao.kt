package com.example.esl.models.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.esl.models.local.relations.PenyewaanWithProperty

@Dao
interface PenyewaanDao {
    @Transaction
    @Query("SELECT * FROM penyewaan WHERE id_penyewaan = :penyewaanId")
    suspend fun getPenyewaanWithProperty(penyewaanId: Int): PenyewaanWithProperty
}

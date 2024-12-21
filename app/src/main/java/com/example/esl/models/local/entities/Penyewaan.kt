package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "penyewaan",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id_users"],
            childColumns = ["id_users"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Property::class,
            parentColumns = ["id_properti"],
            childColumns = ["id_properti"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_users"]), Index(value = ["id_properti"])]
)
data class Penyewaan(
    @PrimaryKey(autoGenerate = true) val id_penyewaan: Int = 0,
    val id_users: Int,
    val id_properti: Int,
    val tanggalMulai: String,
    val tanggalAkhir: String,
    val tanggalOrder: String,
    val masaSewa: Int,
    val tanggalMulai_Update: String?,
    val tanggalAkhir_Update: String?,
    val status: String, // Enum: Aktif, Selesai, Dibatalkan
    val alasan_batal: String?
)

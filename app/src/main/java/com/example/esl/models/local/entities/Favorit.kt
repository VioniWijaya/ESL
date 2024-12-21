package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorit")
data class Favorit(
    @PrimaryKey(autoGenerate = true) val id_favorit: Int = 0,
    val id_users: Int,
    val id_properti: Int,
    val tanggal_ditambahkan: String
)

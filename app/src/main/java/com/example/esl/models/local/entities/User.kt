package com.example.esl.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id_users: Int = 0,
    val nama: String,
    val no_Hp: String,
    val email: String,
    val username: String,
    val password: String,
    val foto_profil: String?,
    val created_at: String,
    val update_at: String?
)

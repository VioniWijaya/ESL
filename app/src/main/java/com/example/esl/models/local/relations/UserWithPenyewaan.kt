package com.example.esl.models.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.esl.models.local.entities.Penyewaan
import com.example.esl.models.local.entities.User

data class UserWithPenyewaan(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id_users",
        entityColumn = "id_users"
    )
    val penyewaanList: List<Penyewaan>
)

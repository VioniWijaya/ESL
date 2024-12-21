package com.example.esl.models.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.esl.models.local.entities.Penyewaan
import com.example.esl.models.local.entities.Property

data class PenyewaanWithProperty(
    @Embedded val penyewaan: Penyewaan,
    @Relation(
        parentColumn = "id_properti",
        entityColumn = "id_properti"
    )
    val property: Property
)

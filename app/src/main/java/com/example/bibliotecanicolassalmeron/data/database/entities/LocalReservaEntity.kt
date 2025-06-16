package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_reservas")
data class LocalReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val reservationDate: String,
    val expirationDate: String,
    val userId: String
)
package com.example.bibliotecanicolassalmeron.data.model

import android.os.Parcelable
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reserva(
    val localId: Int = 0,
    val bookId: String,
    val titulo: String,
    val reservationDate: String,
    val expirationDate: String,
    val userId: String
) : Parcelable

fun Reserva.asReservaEntity(): ReservaEntity {
    return ReservaEntity(
        localId = localId,
        id = "",
        bookId = bookId,
        titulo = titulo,
        reservationDate = reservationDate,
        expirationDate = expirationDate,
        userId = userId
    )
}

package com.example.bibliotecanicolassalmeron.data.model

import android.os.Parcelable
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import kotlinx.parcelize.Parcelize

/**
 * Modelo de datos para una reserva, implementa [Parcelable] para transporte entre componentes Android.
 *
 * @property localId Identificador local de la reserva.
 * @property bookId Identificador del libro reservado.
 * @property titulo Título del libro reservado.
 * @property reservationDate Fecha de reserva.
 * @property expirationDate Fecha de expiración de la reserva.
 * @property userId Identificador del usuario que hizo la reserva.
 */
@Parcelize
data class Reserva(
    val localId: Int = 0,
    val bookId: String,
    val titulo: String,
    val reservationDate: String,
    val expirationDate: String,
    val userId: String
) : Parcelable

/**
 * Extensión para convertir un objeto [Reserva] en una entidad de base de datos [ReservaEntity].
 *
 * @return Entidad para la base de datos con los datos de la reserva.
 */
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

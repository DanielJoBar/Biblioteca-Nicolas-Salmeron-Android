package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa una reserva almacenada localmente.
 *
 * @property id Identificador local autogenerado de la reserva.
 * @property bookId Identificador del libro reservado.
 * @property reservationDate Fecha de la reserva.
 * @property expirationDate Fecha de expiración de la reserva.
 * @property userId Identificador del usuario que realizó la reserva.
 */
@Entity(tableName = "local_reservas")
data class LocalReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val reservationDate: String,
    val expirationDate: String,
    val userId: String
)

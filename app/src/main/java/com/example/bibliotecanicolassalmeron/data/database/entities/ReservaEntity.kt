package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.model.Reserva

/**
 * Entidad de Room que representa una reserva.
 *
 * @property localId Identificador local autogenerado de la reserva.
 * @property id Identificador remoto de la reserva.
 * @property bookId Identificador del libro reservado.
 * @property titulo Título del libro reservado.
 * @property reservationDate Fecha de la reserva.
 * @property expirationDate Fecha de expiración de la reserva.
 * @property userId Identificador del usuario que hizo la reserva.
 */
@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int,
    val id: String,
    val bookId: String,
    val titulo: String,
    val reservationDate: String,
    val expirationDate: String,
    val userId: String
) {
    /**
     * Convierte esta entidad a un modelo de negocio [Reserva].
     *
     * @return Reserva correspondiente a esta entidad.
     */
    fun asReserva(): Reserva {
        return Reserva(
            localId = localId,
            bookId = bookId,
            reservationDate = reservationDate,
            expirationDate = expirationDate,
            userId = userId,
            titulo = titulo
        )
    }
}

/**
 * Extensión para convertir una lista de [ReservaEntity] a una lista de modelos [Reserva].
 *
 * @return Lista de [Reserva] convertida.
 */
fun List<ReservaEntity>.asReservaList(): List<Reserva> {
    return this.map { it.asReserva() }
}

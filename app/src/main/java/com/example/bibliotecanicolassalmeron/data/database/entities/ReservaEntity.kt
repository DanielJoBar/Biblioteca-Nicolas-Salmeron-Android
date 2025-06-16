package com.example.bibliotecanicolassalmeron.data.database.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.model.Reserva

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

fun List<ReservaEntity>.asReservaList(): List<Reserva> {
    return this.map { it.asReserva() }
}
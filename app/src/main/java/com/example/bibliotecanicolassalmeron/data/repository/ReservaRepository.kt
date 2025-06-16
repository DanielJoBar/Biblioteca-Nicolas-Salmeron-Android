package com.example.bibliotecanicolassalmeron.data.repository

import android.util.Log
import com.example.bibliotecanicolassalmeron.data.database.ReservasDatabase
import com.example.bibliotecanicolassalmeron.data.database.entities.asReservaList
import com.example.bibliotecanicolassalmeron.data.model.Reserva
import com.example.bibliotecanicolassalmeron.data.model.asReservaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservaRepository @Inject constructor(
    private val reservasDatabase: ReservasDatabase
) {
    val allReservas: Flow<List<Reserva>>
        get() {
            return reservasDatabase.reservaDao().getReservas().map { list ->
                list.filterNotNull().asReservaList()
            }
        }

    // Inserta una reserva individual
    suspend fun insertReserva(reservaRecibida: Reserva) = withContext(Dispatchers.IO) {
        Log.d("ReservaRepository", "Insertando reserva: $reservaRecibida")
        reservasDatabase.reservaDao().insertReserva(reservaRecibida.asReservaEntity())
    }

    // Elimina una reserva
    suspend fun deleteReserva(reservaRecibida: Reserva) = withContext(Dispatchers.IO) {
        reservasDatabase.reservaDao().deleteReserva(reservaRecibida.asReservaEntity())
    }

     fun getReservasByUserId(userId: String): Flow<List<Reserva>> {
        return reservasDatabase.reservaDao().getReservasByUserId(userId).map { it.asReservaList() }
    }
    suspend fun existsReservation(bookId: String, userId: String): Boolean {
        return reservasDatabase.reservaDao().countByBookAndUser(bookId, userId) > 0
    }
}

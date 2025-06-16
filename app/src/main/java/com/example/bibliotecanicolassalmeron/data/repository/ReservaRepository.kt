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

/**
 * Repositorio para manejar operaciones relacionadas con reservas en la base de datos.
 *
 * @property reservasDatabase Instancia de la base de datos de reservas.
 */
@Singleton
class ReservaRepository @Inject constructor(
    private val reservasDatabase: ReservasDatabase
) {
    /**
     * Flujo reactivo que devuelve la lista completa de reservas en forma de modelo [Reserva].
     */
    val allReservas: Flow<List<Reserva>>
        get() {
            return reservasDatabase.reservaDao().getReservas().map { list ->
                list.filterNotNull().asReservaList()
            }
        }

    /**
     * Inserta una reserva individual en la base de datos.
     *
     * @param reservaRecibida Modelo de reserva a insertar.
     */
    suspend fun insertReserva(reservaRecibida: Reserva) = withContext(Dispatchers.IO) {
        Log.d("ReservaRepository", "Insertando reserva: $reservaRecibida")
        reservasDatabase.reservaDao().insertReserva(reservaRecibida.asReservaEntity())
    }

    /**
     * Elimina una reserva de la base de datos.
     *
     * @param reservaRecibida Modelo de reserva a eliminar.
     */
    suspend fun deleteReserva(reservaRecibida: Reserva) = withContext(Dispatchers.IO) {
        reservasDatabase.reservaDao().deleteReserva(reservaRecibida.asReservaEntity())
    }

    /**
     * Obtiene reservas filtradas por ID de usuario.
     *
     * @param userId Identificador del usuario.
     * @return Flujo reactivo con la lista de reservas filtradas.
     */
    fun getReservasByUserId(userId: String): Flow<List<Reserva>> {
        return reservasDatabase.reservaDao().getReservasByUserId(userId).map { it.asReservaList() }
    }

    /**
     * Verifica si existe una reserva para un libro y usuario dados.
     *
     * @param bookId Identificador del libro.
     * @param userId Identificador del usuario.
     * @return True si existe al menos una reserva, false en caso contrario.
     */
    suspend fun existsReservation(bookId: String, userId: String): Boolean {
        return reservasDatabase.reservaDao().countByBookAndUser(bookId, userId) > 0
    }
}

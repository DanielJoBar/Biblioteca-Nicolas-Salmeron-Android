package com.example.bibliotecanicolassalmeron.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para acceder y manipular las reservas en la base de datos.
 */
@Dao
interface ReservaDao {

    /**
     * Obtiene todas las reservas como un flujo observable.
     *
     * @return Flow que emite listas de [ReservaEntity].
     */
    @Query("SELECT * FROM reservas")
    fun getReservas(): Flow<List<ReservaEntity?>>

    /**
     * Inserta una reserva en la base de datos.
     * Si ya existe, se reemplaza.
     *
     * @param reserva La reserva a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReserva(reserva: ReservaEntity)

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva Reserva con los datos actualizados.
     */
    @Update
    suspend fun updateReserva(reserva: ReservaEntity)

    /**
     * Elimina una reserva específica de la base de datos.
     *
     * @param reserva Reserva a eliminar.
     */
    @Delete
    suspend fun deleteReserva(reserva: ReservaEntity)

    /**
     * Obtiene todas las reservas asociadas a un usuario.
     *
     * @param userId ID del usuario.
     * @return Flow que emite listas de [ReservaEntity] correspondientes al usuario.
     */
    @Query("SELECT * FROM reservas WHERE userId = :userId")
    fun getReservasByUserId(userId: String): Flow<List<ReservaEntity>>

    /**
     * Cuenta cuántas reservas existen para un libro específico y un usuario.
     *
     * @param bookId ID del libro.
     * @param userId ID del usuario.
     * @return Número de reservas encontradas.
     */
    @Query("SELECT COUNT(*) FROM reservas WHERE bookId = :bookId AND userId = :userId")
    suspend fun countByBookAndUser(bookId: String, userId: String): Int
}

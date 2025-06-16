package com.example.bibliotecanicolassalmeron.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Query("SELECT * FROM reservas")
     fun getReservas(): Flow<List<ReservaEntity?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReserva(reserva: ReservaEntity)

    @Update
    suspend fun updateReserva(reserva: ReservaEntity)

    @Delete
    suspend fun deleteReserva(reserva: ReservaEntity)

    @Query("SELECT * FROM reservas WHERE userId = :userId")
    fun getReservasByUserId(userId: String):Flow<List<ReservaEntity>>

    @Query("SELECT COUNT(*) FROM reservas WHERE bookId = :bookId AND userId = :userId")
    suspend fun countByBookAndUser(bookId: String, userId: String): Int

}

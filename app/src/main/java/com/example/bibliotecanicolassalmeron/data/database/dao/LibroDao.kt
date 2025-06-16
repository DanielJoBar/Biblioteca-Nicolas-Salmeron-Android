package com.example.bibliotecanicolassalmeron.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {
    @Query("SELECT * FROM libros")
     fun getLibros(): Flow<List<LibroEntity>>

    @Query("SELECT * FROM libros WHERE localId = :id ")
    suspend fun getOneLibro(id:Int):LibroEntity?

    @Query("SELECT * FROM libros WHERE genero = :genre")
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibros(libros: List<LibroEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibro(libro: LibroEntity)

    @Update
    suspend fun updateLibro(libro: LibroEntity)

    @Delete
    suspend fun deleteLibro(libro: LibroEntity)


}

package com.example.bibliotecanicolassalmeron.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para acceder y manipular los datos de libros en la base de datos.
 */
@Dao
interface LibroDao {

    /**
     * Obtiene todos los libros almacenados en la base de datos como un flujo observable.
     *
     * @return Flow que emite listas de [LibroEntity].
     */
    @Query("SELECT * FROM libros")
    fun getLibros(): Flow<List<LibroEntity>>

    /**
     * Recupera un libro específico por su identificador local.
     *
     * @param id ID local del libro.
     * @return El [LibroEntity] correspondiente o null si no se encuentra.
     */
    @Query("SELECT * FROM libros WHERE localId = :id ")
    suspend fun getOneLibro(id: Int): LibroEntity?

    /**
     * Recupera los libros filtrados por género como flujo observable.
     *
     * @param genre Género del libro.
     * @return Flow que emite listas de [LibroEntity] con el género especificado.
     */
    @Query("SELECT * FROM libros WHERE genero = :genre")
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>>

    /**
     * Inserta una lista de libros en la base de datos.
     * Si ya existe un libro con el mismo ID, se reemplaza.
     *
     * @param libros Lista de libros a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibros(libros: List<LibroEntity>)

    /**
     * Inserta un único libro en la base de datos.
     * Si ya existe un libro con el mismo ID, se reemplaza.
     *
     * @param libro El libro a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibro(libro: LibroEntity)

    /**
     * Actualiza un libro existente en la base de datos.
     *
     * @param libro Libro con los datos actualizados.
     */
    @Update
    suspend fun updateLibro(libro: LibroEntity)

    /**
     * Elimina un libro específico de la base de datos.
     *
     * @param libro Libro que se desea eliminar.
     */
    @Delete
    suspend fun deleteLibro(libro: LibroEntity)
}

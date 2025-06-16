package com.example.bibliotecanicolassalmeron.data.database

import androidx.annotation.WorkerThread
import com.example.bibliotecanicolassalmeron.data.database.dao.LibroDao
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.data.model.asLibroEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio local que gestiona operaciones de acceso a datos de libros usando Room.
 *
 * Esta clase actúa como intermediario entre el DAO y las capas superiores.
 *
 * @constructor Crea una instancia del repositorio inyectando [LibroDao].
 * @param libroDao DAO para acceso a datos de libros.
 */
@Singleton
class BibliotecaLocalRepository @Inject constructor(
    private val libroDao: LibroDao
) {

    /**
     * Flujo que emite la lista completa de libros almacenados localmente.
     */
    val allLibrosList: Flow<List<LibroEntity>> = libroDao.getLibros()

    /**
     * Inserta un libro en la base de datos.
     *
     * @param listLibro Libro a insertar, representado por el modelo de dominio [Libro].
     */
    @WorkerThread
    suspend fun insert(listLibro: Libro) {
        libroDao.insertLibro(listLibro.asLibroEntity())
    }

    /**
     * Inserta múltiples libros en la base de datos.
     *
     * @param libros Lista de entidades [LibroEntity] a insertar.
     */
    @WorkerThread
    suspend fun insertLibros(libros: List<LibroEntity>) {
        libroDao.insertLibros(libros)
    }

    /**
     * Obtiene un libro por su identificador local.
     *
     * @param id Identificador del libro.
     * @return Instancia de [LibroEntity] si existe, o null si no se encuentra.
     */
    @WorkerThread
    suspend fun getOne(id: Int): LibroEntity? {
        return libroDao.getOneLibro(id)
    }

    /**
     * Obtiene libros filtrados por género.
     *
     * @param genre Género por el cual se filtrarán los libros.
     * @return Flow que emite listas de libros correspondientes al género indicado.
     */
    @WorkerThread
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>> {
        return libroDao.getLibrosByGenre(genre)
    }

    /**
     * Actualiza los datos de un libro existente en la base de datos.
     *
     * @param libro Libro con los nuevos datos, representado por el modelo de dominio [Libro].
     */
    @WorkerThread
    suspend fun updateLibro(libro: Libro) {
        libroDao.updateLibro(libro.asLibroEntity())
    }

    /**
     * Elimina un libro de la base de datos.
     *
     * @param libro Libro a eliminar, representado por el modelo de dominio [Libro].
     */
    @WorkerThread
    suspend fun deleteLibro(libro: Libro) {
        libroDao.deleteLibro(libro.asLibroEntity())
    }
}

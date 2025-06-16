package com.example.bibliotecanicolassalmeron.data.repository

import android.util.Log
import com.example.bibliotecanicolassalmeron.data.database.BibliotecaLocalRepository
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.database.entities.asLibroList
import com.example.bibliotecanicolassalmeron.data.model.Libro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para gestionar operaciones relacionadas con libros en la base de datos local.
 *
 * @property localRepository Repositorio local de la base de datos.
 */
@Singleton
class LibroRepository @Inject constructor (
    private val localRepository: BibliotecaLocalRepository,
) {
    /**
     * Flujo reactivo que devuelve la lista completa de libros en forma de modelo [Libro].
     */
    val allLibros: Flow<List<Libro>>
        get() {
            return localRepository.allLibrosList.map { list ->
                list.asLibroList()
            }
        }

    /**
     * Obtiene un libro por su id desde la base de datos de manera suspendida.
     *
     * @param id Identificador del libro.
     * @return Entidad [LibroEntity] correspondiente o null si no existe.
     */
    suspend fun getOneFromDatabase(id: Int): LibroEntity? = withContext(Dispatchers.IO) {
        localRepository.getOne(id)
    }

    /**
     * Inserta un libro individual en la base de datos.
     *
     * @param libroRecibido Modelo de libro a insertar.
     */
    suspend fun insertLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        Log.d("LibroRepository", "Insertando libro: $libroRecibido")
        localRepository.insert(libroRecibido)
    }

    /**
     * Inserta una lista de libros en la base de datos.
     *
     * @param libros Lista de entidades de libro a insertar.
     */
    suspend fun insertLibros(libros: List<LibroEntity>) = withContext(Dispatchers.IO) {
        localRepository.insertLibros(libros)
    }

    /**
     * Actualiza un libro existente en la base de datos.
     *
     * @param libroRecibido Modelo de libro con los datos actualizados.
     */
    suspend fun updateLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        localRepository.updateLibro(libroRecibido)
    }

    /**
     * Elimina un libro de la base de datos.
     *
     * @param libroRecibido Modelo de libro a eliminar.
     */
    suspend fun deleteLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        localRepository.deleteLibro(libroRecibido)
    }

    /**
     * Obtiene libros filtrados por género.
     *
     * @param genre Género para filtrar los libros.
     * @return Flujo reactivo con la lista de libros filtrados.
     */
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>> {
        return localRepository.getLibrosByGenre(genre)
    }
}

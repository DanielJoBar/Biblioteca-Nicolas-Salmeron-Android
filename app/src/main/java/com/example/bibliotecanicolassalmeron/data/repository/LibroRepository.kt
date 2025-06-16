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

@Singleton
class LibroRepository @Inject constructor (
    private val localRepository: BibliotecaLocalRepository,
    ) {
        val allLibros: Flow<List<Libro>>
            get() {
                return localRepository.allLibrosList.map {
                        list -> list.asLibroList()
                }
            }

    suspend fun getOneFromDatabase(id: Int): LibroEntity? = withContext(Dispatchers.IO) {
        localRepository.getOne(id)
    }

    // Inserta un libro individual
    suspend fun insertLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        Log.d("LibroRepository", "Insertando libro: $libroRecibido")
        localRepository.insert(libroRecibido)
    }

    // Inserta una lista de libros
    suspend fun insertLibros(libros: List<LibroEntity>) = withContext(Dispatchers.IO) {
        localRepository.insertLibros(libros)
    }

    // Actualiza un libro
    suspend fun updateLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        localRepository.updateLibro(libroRecibido)
    }

    // Elimina un libro
    suspend fun deleteLibro(libroRecibido: Libro) = withContext(Dispatchers.IO) {
        localRepository.deleteLibro(libroRecibido)
    }

    // Obtiene libros filtrados por género (retorna el Flow directamente, ya que la consulta es reactiva)
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>> {
        return localRepository.getLibrosByGenre(genre)
    }
}
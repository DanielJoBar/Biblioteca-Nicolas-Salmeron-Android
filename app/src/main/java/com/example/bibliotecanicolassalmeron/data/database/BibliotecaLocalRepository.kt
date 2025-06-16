package com.example.bibliotecanicolassalmeron.data.database

import androidx.annotation.WorkerThread
import com.example.bibliotecanicolassalmeron.data.database.dao.LibroDao
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.data.model.asLibroEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BibliotecaLocalRepository @Inject constructor(
    private val libroDao: LibroDao
) {
    val allLibrosList: Flow<List<LibroEntity>> = libroDao.getLibros()

    @WorkerThread
    suspend fun insert(listLibro: Libro) {
        libroDao.insertLibro(listLibro.asLibroEntity())
    }
    @WorkerThread
    suspend fun insertLibros(libros: List<LibroEntity>) {
        libroDao.insertLibros(libros)
    }

    // Obtiene un libro concreto mediante su nombre.
    @WorkerThread
    suspend fun getOne(id: Int): LibroEntity? {
        return libroDao.getOneLibro(id)
    }

    @WorkerThread
    fun getLibrosByGenre(genre: String): Flow<List<LibroEntity>> {
        return libroDao.getLibrosByGenre(genre)
    }
    @WorkerThread
    suspend fun updateLibro(libro: Libro) {
        libroDao.updateLibro(libro.asLibroEntity())
    }

    @WorkerThread
    suspend fun deleteLibro(libro: Libro) {
        libroDao.deleteLibro(libro.asLibroEntity())
    }
}
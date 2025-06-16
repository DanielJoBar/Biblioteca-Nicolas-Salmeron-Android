package com.example.bibliotecanicolassalmeron.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.data.repository.LibroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar la lista de libros.
 *
 * Expone un StateFlow con la lista de libros, y métodos para insertar, actualizar y eliminar libros.
 *
 * @property libroRepository Repositorio para acceder a los datos de libros.
 */
@HiltViewModel
class LibroListViewModel @Inject constructor(
    private val libroRepository: LibroRepository
) : ViewModel() {

    /** Estado privado que contiene la lista actual de libros */
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())

    /** Estado público para observar la lista de libros */
    val libros: StateFlow<List<Libro>> get() = _libros

    init {
        // Se suscribe a los cambios en la lista de libros desde el repositorio
        viewModelScope.launch {
            libroRepository.allLibros.collect { nuevaLista ->
                Log.d("LibroViewModel", "Lista actualizada: ${nuevaLista.size} libros")
                _libros.value = nuevaLista
            }
        }
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param newLibro Libro a insertar.
     * @param onComplete Callback que se ejecuta al completar la inserción.
     */
    fun insertLibro(newLibro: Libro, onComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("LibroViewModel", "Insertando desde ViewModel: $newLibro")
            libroRepository.insertLibro(newLibro)
            onComplete()
        }
    }

    /**
     * Actualiza un libro existente en la base de datos.
     *
     * @param updatedLibro Libro con los datos actualizados.
     * @param onComplete Callback que se ejecuta al completar la actualización.
     */
    fun updateLibro(updatedLibro: Libro, onComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("LibroViewModel", "Insertando desde ViewModel: $updatedLibro")
            libroRepository.updateLibro(updatedLibro)
            onComplete()
        }
    }

    /**
     * Elimina un libro de la base de datos.
     *
     * @param libro Libro a eliminar.
     */
    fun deleteLibro(libro: Libro) {
        viewModelScope.launch {
            libroRepository.deleteLibro(libro)
        }
    }
}

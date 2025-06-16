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

@HiltViewModel
class LibroListViewModel @Inject constructor(
    private val libroRepository: LibroRepository
) : ViewModel() {

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> get() = _libros

    init {
        viewModelScope.launch {
            libroRepository.allLibros.collect { nuevaLista ->
                Log.d("LibroViewModel", "Lista actualizada: ${nuevaLista.size} libros")
                _libros.value = nuevaLista
            }
        }
    }

    fun insertLibro(newLibro: Libro, onComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("LibroViewModel", "Insertando desde ViewModel: $newLibro")
            libroRepository.insertLibro(newLibro)
            onComplete()
        }
    }

    fun updateLibro(updatedLibro: Libro, onComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("LibroViewModel", "Insertando desde ViewModel: $updatedLibro")
            libroRepository.updateLibro(updatedLibro)
            onComplete()
        }
    }
    fun deleteLibro(libro: Libro) {
        viewModelScope.launch {
            libroRepository.deleteLibro(libro)
        }
    }
}


package com.example.bibliotecanicolassalmeron.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioListViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    fun deleteUserByEmail(email: String, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                onError("El email no es válido")
                return@launch
            }
            try {
                usuarioRepository.deleteUserByEmail(email)
                onComplete()
            } catch (e: Exception) {
                onError("Error al borrar usuario: ${e.message}")
            }
        }
    }
}
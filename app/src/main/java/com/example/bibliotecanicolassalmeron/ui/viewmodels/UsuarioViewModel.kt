package com.example.bibliotecanicolassalmeron.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para manejar operaciones relacionadas con usuarios, como eliminar un usuario por email.
 *
 * @property usuarioRepository Repositorio para operaciones de usuario.
 */
@HiltViewModel
class UsuarioListViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    /**
     * Elimina un usuario dado su email.
     *
     * @param email Correo electrónico del usuario a eliminar.
     * @param onComplete Callback llamado si la eliminación fue exitosa.
     * @param onError Callback llamado con un mensaje de error si ocurre alguna falla.
     */
    fun deleteUserByEmail(email: String, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                onError("El email no es válido") // Se debería extraer a strings.xml
                return@launch
            }
            try {
                usuarioRepository.deleteUserByEmail(email)
                onComplete()
            } catch (e: Exception) {
                onError("Error al borrar usuario: ${e.message}") // Se debería extraer a strings.xml, excepto el mensaje de excepción dinámico
            }
        }
    }
}

package com.example.bibliotecanicolassalmeron.data.api

import javax.inject.Inject

class AuthApiRepository @Inject constructor(
    private val service: BibliotecaService
) {
    // Realiza el login
    suspend fun signIn(identifier: String, password: String): LoginResponse {
        val request = LoginRequest(identifier, password)
        return service.api.signIn(request)
    }

    // Realiza el proceso de registro
    suspend fun signUp(
        username: String,
        email: String,
        password: String,
        nombre: String,
        apellido: String
    ): RegisterResponse {
        val request = RegisterRequest(username, email, password, nombre, apellido)
        return service.api.signUp(request)
    }

    // Obtiene la información del usuario actual
    suspend fun getCurrentUser(): UserResponse = service.api.getCurrentUser()
}

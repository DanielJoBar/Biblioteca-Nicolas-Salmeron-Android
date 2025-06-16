package com.example.bibliotecanicolassalmeron.data.api

data class LoginRequest(
    val identifier: String, // Puede ser el nombre de usuario o email
    val password: String
)

// Respuesta de login
data class LoginResponse(
    val jwt: String,
    val user: UserResponse
)

// Solicitud de registro
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String
)

// Respuesta de registro (puede ser similar a LoginResponse)
data class RegisterResponse(
    val jwt: String,
    val user: UserResponse
)

// Información del usuario
data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String
)

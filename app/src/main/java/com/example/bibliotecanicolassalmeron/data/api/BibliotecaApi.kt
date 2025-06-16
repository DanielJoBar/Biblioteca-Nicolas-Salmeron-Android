package com.example.bibliotecanicolassalmeron.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BibliotecaApi {

    @POST("api/auth/local")
    suspend fun signIn(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/local/register")
    suspend fun signUp(@Body request: RegisterRequest): RegisterResponse

    @GET("api/users/me")
    suspend fun getCurrentUser(): UserResponse

}

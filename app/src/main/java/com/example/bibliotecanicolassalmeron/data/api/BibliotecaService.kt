package com.example.bibliotecanicolassalmeron.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BibliotecaService @Inject constructor() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://biblioteca-service.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: BibliotecaApi = retrofit.create(BibliotecaApi::class.java)
}

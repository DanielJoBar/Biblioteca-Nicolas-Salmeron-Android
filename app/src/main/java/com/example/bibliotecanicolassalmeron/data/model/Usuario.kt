package com.example.bibliotecanicolassalmeron.data.repository.models

data class Usuario(
    val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val picture: Picture
)

data class Picture(
    val large: String,
    val small: String,
    val thumbnail: String,
    val url: String
)

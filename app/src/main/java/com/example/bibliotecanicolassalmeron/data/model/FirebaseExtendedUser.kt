package com.example.bibliotecanicolassalmeron.data.model

import com.example.bibliotecanicolassalmeron.data.repository.models.Picture

data class FirebaseExtendedUser(
    val id: String = "",
    val UID: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val password: String,
    val picture: Picture
)

data class Picture(
    val large: String,
    val small: String,
    val thumbnail: String,
    val url: String
)

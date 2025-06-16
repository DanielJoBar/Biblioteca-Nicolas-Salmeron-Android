package com.example.bibliotecanicolassalmeron.data.model

data class FirebaseLibro(
    val id: String = "",    // Opcional, según se reciba o no
    val author: String,
    val genre: String,
    val isbn: String,
    val picture: String,
    val summary: String,
    val title: String
)
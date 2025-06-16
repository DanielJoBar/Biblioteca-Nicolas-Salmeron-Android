package com.example.bibliotecanicolassalmeron.data.model

data class FirebaseReserva(
    val id: String = "",
    val bookId: String,
    val reservationDate: String,
    val expirationDate: String,
    val userID: String
)

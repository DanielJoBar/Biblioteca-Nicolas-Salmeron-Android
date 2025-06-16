package com.example.bibliotecanicolassalmeron.data.model

import android.os.Parcelable
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class Libro(
    val id: Int,
    val titulo: String,
    val author: String,
    val genero: String,
    val isbn: String,
    val imagen: String,
    val resumen: String,
) : Parcelable

fun Libro.asLibroEntity(): LibroEntity {
    return LibroEntity(
        localId = id,
        bookId = "",
        titulo = titulo,
        author = author,
        genero = genero,
        isbn = isbn,
        imagen = imagen,
        resumen = resumen
    )
}
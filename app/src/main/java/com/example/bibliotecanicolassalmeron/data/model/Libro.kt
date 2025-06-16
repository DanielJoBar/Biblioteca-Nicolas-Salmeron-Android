package com.example.bibliotecanicolassalmeron.data.model

import android.os.Parcelable
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import kotlinx.parcelize.Parcelize

/**
 * Modelo de datos para un libro, implementa [Parcelable] para pasar entre componentes Android.
 *
 * @property id Identificador local del libro.
 * @property titulo Título del libro.
 * @property author Autor del libro.
 * @property genero Género del libro.
 * @property isbn Código ISBN del libro.
 * @property imagen URL o ruta de la imagen de portada.
 * @property resumen Resumen o sinopsis del libro.
 */
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

/**
 * Extensión para convertir un objeto [Libro] en una entidad de base de datos [LibroEntity].
 *
 * @return Entidad para base de datos con los datos del libro.
 */
fun Libro.asLibroEntity(): LibroEntity {
    return LibroEntity(
        localId = id,
        bookId = "", // Sin bookId asignado aquí
        titulo = titulo,
        author = author,
        genero = genero,
        isbn = isbn,
        imagen = imagen,
        resumen = resumen
    )
}

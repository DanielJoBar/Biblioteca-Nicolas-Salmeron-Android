package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa un libro almacenado localmente.
 *
 * @property id Identificador local autogenerado del libro.
 * @property titulo Título del libro.
 * @property author Autor del libro.
 * @property genero Género del libro.
 * @property isbn Código ISBN del libro.
 * @property imagen Ruta o URL de la imagen del libro.
 * @property resumen Resumen o sinopsis del libro.
 */
@Entity(tableName = "local_libros")
data class LocalLibroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val author: String,
    val genero: String,
    val isbn: String,
    val imagen: String,
    val resumen: String
)

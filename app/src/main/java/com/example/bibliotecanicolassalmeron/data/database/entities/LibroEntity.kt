package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.model.Libro

/**
 * Entidad de Room que representa un libro en la base de datos local.
 *
 * @property localId Identificador local autogenerado del libro.
 * @property bookId Identificador externo o remoto del libro.
 * @property titulo Título del libro.
 * @property author Autor del libro.
 * @property genero Género del libro.
 * @property isbn Código ISBN del libro.
 * @property imagen URL o ruta de la imagen del libro.
 * @property resumen Resumen o sinopsis del libro.
 */
@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int,
    val bookId : String,
    val titulo: String,
    val author: String,
    val genero: String,
    val isbn: String,
    val imagen: String,
    val resumen: String
) {
    /**
     * Convierte esta entidad en un objeto de dominio [Libro].
     *
     * @return Un objeto [Libro] con los mismos datos.
     */
    fun asLibro(): Libro {
        return Libro(
            id = localId,
            titulo = titulo,
            author = author,
            genero = genero,
            isbn = isbn,
            imagen = imagen,
            resumen = resumen)
    }
}

/**
 * Extensión para convertir una lista de entidades [LibroEntity] a una lista de objetos de dominio [Libro].
 *
 * @return Lista de [Libro].
 */
fun List<LibroEntity>.asLibroList(): List<Libro> {
    return this.map { it.asLibro() }
}

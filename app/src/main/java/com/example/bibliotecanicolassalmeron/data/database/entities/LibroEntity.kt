package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.model.Libro


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

fun List<LibroEntity>.asLibroList(): List<Libro> {
    return this.map { it.asLibro() }
}
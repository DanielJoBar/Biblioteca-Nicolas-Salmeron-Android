package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseLibro
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class LibroMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseLibro>): Paginated<LibroEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    fun getOne(data: FirebaseLibro): LibroEntity {
        return LibroEntity(
            localId = 0,
            bookId = data.id,
            titulo = data.title,
            author = data.author,
            genero = data.genre,
            isbn = data.isbn,
            imagen = data.picture,
            resumen = data.summary
        )
    }

    fun setAdd(entity: LibroEntity): FirebaseLibro {
        return FirebaseLibro(
            // Si se requiere enviar el id, usa entity.bookId; de lo contrario, se ignora al insertar.
            author = entity.author,
            genre = entity.genero,
            isbn = entity.isbn,
            picture = entity.imagen,
            summary = entity.resumen,
            title = entity.titulo
        )
    }

    fun setUpdate(entity: LibroEntity): FirebaseLibro {
        return FirebaseLibro(
            id = entity.bookId,
            author = entity.author,
            genre = entity.genero,
            isbn = entity.isbn,
            picture = entity.imagen,
            summary = entity.resumen,
            title = entity.titulo
        )
    }
}

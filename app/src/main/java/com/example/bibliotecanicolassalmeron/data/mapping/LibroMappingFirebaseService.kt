package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseLibro
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

/**
 * Servicio para mapear entre [FirebaseLibro] y la entidad local [LibroEntity].
 *
 * @property firestore Instancia de FirebaseFirestore para acceso a datos remotos.
 */
@Singleton
class LibroMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Convierte una lista paginada de [FirebaseLibro] a un objeto paginado de [LibroEntity].
     *
     * @param page Página actual.
     * @param pageSize Tamaño de la página.
     * @param pages Total de elementos.
     * @param data Lista de libros Firebase.
     * @return Objeto paginado con entidades locales de libros.
     */
    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseLibro>): Paginated<LibroEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    /**
     * Convierte un [FirebaseLibro] en una entidad local [LibroEntity].
     *
     * @param data Libro en formato Firebase.
     * @return Entidad local del libro.
     */
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

    /**
     * Convierte una entidad local [LibroEntity] en un modelo [FirebaseLibro] para agregar.
     *
     * Nota: El id no se envía al agregar, solo se genera en Firebase.
     *
     * @param entity Entidad local del libro.
     * @return Modelo Firebase para inserción.
     */
    fun setAdd(entity: LibroEntity): FirebaseLibro {
        return FirebaseLibro(
            author = entity.author,
            genre = entity.genero,
            isbn = entity.isbn,
            picture = entity.imagen,
            summary = entity.resumen,
            title = entity.titulo
        )
    }

    /**
     * Convierte una entidad local [LibroEntity] en un modelo [FirebaseLibro] para actualizar.
     *
     * @param entity Entidad local del libro.
     * @return Modelo Firebase para actualización.
     */
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

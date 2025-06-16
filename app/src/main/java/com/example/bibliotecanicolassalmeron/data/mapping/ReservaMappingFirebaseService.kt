package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseReserva
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

/**
 * Servicio para mapear entre [FirebaseReserva] y la entidad local [ReservaEntity].
 *
 * @property firestore Instancia de FirebaseFirestore para acceso a datos remotos.
 */
@Singleton
class ReservaMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Convierte una lista paginada de [FirebaseReserva] a un objeto paginado de [ReservaEntity].
     *
     * @param page Página actual.
     * @param pageSize Tamaño de la página.
     * @param pages Total de elementos.
     * @param data Lista de reservas Firebase.
     * @return Objeto paginado con entidades locales de reservas.
     */
    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseReserva>): Paginated<ReservaEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    /**
     * Convierte un [FirebaseReserva] en una entidad local [ReservaEntity].
     *
     * @param data Reserva en formato Firebase.
     * @return Entidad local de reserva.
     */
    fun getOne(data: FirebaseReserva): ReservaEntity {
        return ReservaEntity(
            localId = 0,
            id = data.id,
            bookId = data.bookId,
            reservationDate = data.reservationDate,
            expirationDate = data.expirationDate,
            userId = data.userID,
            titulo = "" // Título vacío porque no está en FirebaseReserva
        )
    }

    /**
     * Convierte una entidad local [ReservaEntity] en un modelo [FirebaseReserva] para agregar.
     *
     * @param entity Entidad local de reserva.
     * @return Modelo Firebase para inserción.
     */
    fun setAdd(entity: ReservaEntity): FirebaseReserva {
        return FirebaseReserva(
            id = entity.id,
            bookId = entity.bookId,
            reservationDate = entity.reservationDate,
            expirationDate = entity.expirationDate,
            userID = entity.userId
        )
    }

    /**
     * Convierte una entidad local [ReservaEntity] en un modelo [FirebaseReserva] para actualizar.
     *
     * @param entity Entidad local de reserva.
     * @return Modelo Firebase para actualización.
     */
    fun setUpdate(entity: ReservaEntity): FirebaseReserva {
        return FirebaseReserva(
            id = entity.id,
            bookId = entity.bookId,
            reservationDate = entity.reservationDate,
            expirationDate = entity.expirationDate,
            userID = entity.userId
        )
    }
}

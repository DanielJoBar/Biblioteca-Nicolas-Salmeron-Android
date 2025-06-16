package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseReserva
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class ReservaMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseReserva>): Paginated<ReservaEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    fun getOne(data: FirebaseReserva): ReservaEntity {
        return ReservaEntity(
            localId = 0,
            id = data.id,
            bookId = data.bookId,
            reservationDate = data.reservationDate,
            expirationDate = data.expirationDate,
            userId = data.userID,
            titulo = ""
        )
    }

    fun setAdd(entity: ReservaEntity): FirebaseReserva {
        return FirebaseReserva(
            id = entity.id,
            bookId = entity.bookId,
            reservationDate = entity.reservationDate,
            expirationDate = entity.expirationDate,
            userID = entity.userId
        )
    }

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

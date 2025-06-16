package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseExtendedUser
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.example.bibliotecanicolassalmeron.data.repository.models.Picture
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class ExtendedUserMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getOne(data: FirebaseExtendedUser): UsuarioEntity {
        return UsuarioEntity(
            localId = 0,
            id = data.id,
            name = data.name,
            surname = data.surname,
            username = data.username,
            email = data.email,
            password = data.password,
            pictureLarge = data.picture.large,
            pictureSmall = data.picture.small,
            pictureThumbnail = data.picture.thumbnail,
            pictureUrl = data.picture.url
        )
    }

    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseExtendedUser>): Paginated<UsuarioEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    fun setAdd(entity: UsuarioEntity): FirebaseExtendedUser {
        return FirebaseExtendedUser(
            UID = entity.id,
            name = entity.name,
            surname = entity.surname,
            username = entity.username,
            email = entity.email,
            password = entity.password,
            picture =  Picture(
                large = entity.pictureLarge,
                small = entity.pictureSmall,
                thumbnail = entity.pictureThumbnail,
                url = entity.pictureUrl
            )
        )
    }

    fun setUpdate(entity: UsuarioEntity): FirebaseExtendedUser {
        return FirebaseExtendedUser(
            id = entity.id,
            UID = entity.id,
            name = entity.name,
            surname = entity.surname,
            username = entity.username,
            email = entity.email,
            password = entity.password,
            picture =  Picture(
                large = entity.pictureLarge,
                small = entity.pictureSmall,
                thumbnail = entity.pictureThumbnail,
                url = entity.pictureUrl
            )
        )
    }
}


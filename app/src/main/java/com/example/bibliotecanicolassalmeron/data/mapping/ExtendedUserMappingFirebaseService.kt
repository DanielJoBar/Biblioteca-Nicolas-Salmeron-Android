package com.example.bibliotecanicolassalmeron.data.mapping

import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.model.FirebaseExtendedUser
import com.example.bibliotecanicolassalmeron.data.model.Paginated
import com.example.bibliotecanicolassalmeron.data.repository.models.Picture
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

/**
 * Servicio que mapea objetos entre modelos de usuario extendido de Firebase y entidades locales.
 *
 * @property firestore Instancia de FirebaseFirestore para acceso a datos remotos.
 */
@Singleton
class ExtendedUserMappingFirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Convierte un [FirebaseExtendedUser] a su correspondiente entidad local [UsuarioEntity].
     *
     * @param data Usuario extendido de Firebase.
     * @return Entidad local de usuario.
     */
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

    /**
     * Genera un objeto paginado de entidades de usuario a partir de una lista de usuarios Firebase.
     *
     * @param page Página actual.
     * @param pageSize Tamaño de página.
     * @param pages Número total de elementos.
     * @param data Lista de usuarios extendidos de Firebase.
     * @return Objeto paginado con entidades de usuario.
     */
    fun getPaginated(page: Int, pageSize: Int, pages: Int, data: List<FirebaseExtendedUser>): Paginated<UsuarioEntity> {
        return Paginated(
            data = data.map { getOne(it) },
            page = page,
            pageSize = pageSize,
            pages = ceil(pages.toDouble() / pageSize).toInt()
        )
    }

    /**
     * Convierte una entidad local [UsuarioEntity] a un modelo Firebase para añadir.
     *
     * @param entity Entidad local de usuario.
     * @return Modelo Firebase extendido de usuario.
     */
    fun setAdd(entity: UsuarioEntity): FirebaseExtendedUser {
        return FirebaseExtendedUser(
            UID = entity.id,
            name = entity.name,
            surname = entity.surname,
            username = entity.username,
            email = entity.email,
            password = entity.password,
            picture = Picture(
                large = entity.pictureLarge,
                small = entity.pictureSmall,
                thumbnail = entity.pictureThumbnail,
                url = entity.pictureUrl
            )
        )
    }

    /**
     * Convierte una entidad local [UsuarioEntity] a un modelo Firebase para actualizar.
     *
     * @param entity Entidad local de usuario.
     * @return Modelo Firebase extendido de usuario.
     */
    fun setUpdate(entity: UsuarioEntity): FirebaseExtendedUser {
        return FirebaseExtendedUser(
            id = entity.id,
            UID = entity.id,
            name = entity.name,
            surname = entity.surname,
            username = entity.username,
            email = entity.email,
            password = entity.password,
            picture = Picture(
                large = entity.pictureLarge,
                small = entity.pictureSmall,
                thumbnail = entity.pictureThumbnail,
                url = entity.pictureUrl
            )
        )
    }
}

package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.repository.models.Picture
import com.example.bibliotecanicolassalmeron.data.repository.models.Usuario

/**
 * Entidad de Room que representa un usuario en la base de datos local.
 *
 * @property localId Identificador local autogenerado.
 * @property id Identificador remoto del usuario.
 * @property name Nombre del usuario.
 * @property surname Apellidos del usuario.
 * @property username Nombre de usuario.
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 * @property pictureLarge URL de la imagen grande del usuario.
 * @property pictureSmall URL de la imagen pequeña del usuario.
 * @property pictureThumbnail URL de la miniatura de la imagen del usuario.
 * @property pictureUrl URL genérica de la imagen del usuario.
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val password: String,
    val pictureLarge: String,
    val pictureSmall: String,
    val pictureThumbnail: String,
    val pictureUrl: String
) {
    /**
     * Convierte esta entidad a un modelo de dominio Usuario.
     *
     * @return Instancia de [Usuario] con los datos de esta entidad.
     */
    fun asUsuario(): Usuario {
        return Usuario(
            id = id,
            name = name,
            surname = surname,
            username = username,
            email = email,
            picture = Picture(
                large = pictureLarge,
                small = pictureSmall,
                thumbnail = pictureThumbnail,
                url = pictureUrl
            )
        )
    }
}

/**
 * Extensión para convertir una lista de [UsuarioEntity] a una lista de modelos [Usuario].
 *
 * @return Lista de usuarios convertidos.
 */
fun List<UsuarioEntity>.asUsuarioList(): List<Usuario> {
    return this.map { it.asUsuario() }
}

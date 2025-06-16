package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibliotecanicolassalmeron.data.repository.models.Picture
import com.example.bibliotecanicolassalmeron.data.repository.models.Usuario

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


fun List<UsuarioEntity>.asUsuarioList(): List<Usuario> {
    return this.map { it.asUsuario() }
}
package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa un usuario almacenado localmente.
 *
 * @property id Identificador único del usuario, autogenerado.
 * @property name Nombre del usuario.
 * @property surname Apellidos del usuario.
 * @property username Nombre de usuario.
 * @property email Correo electrónico del usuario.
 * @property picture URL o ruta de la imagen de perfil del usuario.
 */
@Entity(tableName = "local_usuarios")
data class LocalUsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val picture: String
)

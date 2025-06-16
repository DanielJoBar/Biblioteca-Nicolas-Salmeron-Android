package com.example.bibliotecanicolassalmeron.data.repository.models

/**
 * Modelo que representa un usuario.
 *
 * @property id Identificador único del usuario.
 * @property name Nombre del usuario.
 * @property surname Apellidos del usuario.
 * @property username Nombre de usuario.
 * @property email Correo electrónico del usuario.
 * @property picture Información de las imágenes del usuario.
 */
data class Usuario(
    val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val picture: Picture
)

/**
 * Modelo que representa las distintas URLs de imágenes de un usuario.
 *
 * @property large Imagen de tamaño grande.
 * @property small Imagen de tamaño pequeño.
 * @property thumbnail Imagen en miniatura.
 * @property url URL genérica de la imagen.
 */
data class Picture(
    val large: String,
    val small: String,
    val thumbnail: String,
    val url: String
)

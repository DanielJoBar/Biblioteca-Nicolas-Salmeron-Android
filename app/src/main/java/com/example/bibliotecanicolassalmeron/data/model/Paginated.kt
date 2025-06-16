package com.example.bibliotecanicolassalmeron.data.model

/**
 * Clase genérica para manejar paginación de datos.
 *
 * @param T Tipo de los elementos de la lista paginada.
 * @property data Lista de elementos de la página actual.
 * @property page Número de página actual.
 * @property pageSize Cantidad de elementos por página.
 * @property pages Total de páginas disponibles.
 */
data class Paginated<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val pages: Int
)

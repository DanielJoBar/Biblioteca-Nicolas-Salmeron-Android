package com.example.bibliotecanicolassalmeron.data.model

data class Paginated<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val pages: Int
)

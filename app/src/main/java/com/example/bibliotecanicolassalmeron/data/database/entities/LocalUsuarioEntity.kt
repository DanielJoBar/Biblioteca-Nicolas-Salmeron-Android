package com.example.bibliotecanicolassalmeron.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_usuarios")
data class LocalUsuarioEntity(
    @PrimaryKey(autoGenerate = true)   val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val picture: String
)
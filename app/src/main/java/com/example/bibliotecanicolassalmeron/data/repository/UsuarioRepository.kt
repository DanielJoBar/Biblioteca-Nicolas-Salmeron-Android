package com.example.bibliotecanicolassalmeron.data.repository

import com.example.bibliotecanicolassalmeron.data.database.dao.UsuarioDao
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
    fun getUsuarios(): Flow<List<UsuarioEntity>> = usuarioDao.getUsuarios()

    suspend fun insertUsuario(usuario: UsuarioEntity) = usuarioDao.insertUsuario(usuario)
    suspend fun getUserByEmail(email: String): UsuarioEntity? = usuarioDao.getUserByEmail(email)
    suspend fun deleteUsuario(usuario: UsuarioEntity) = usuarioDao.deleteUsuario(usuario)
    suspend fun deleteUserByEmail(email: String) = usuarioDao.deleteUserByEmail(email)

}

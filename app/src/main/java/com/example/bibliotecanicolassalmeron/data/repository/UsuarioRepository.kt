package com.example.bibliotecanicolassalmeron.data.repository

import com.example.bibliotecanicolassalmeron.data.database.dao.UsuarioDao
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repositorio para gestionar operaciones relacionadas con usuarios en la base de datos.
 *
 * @property usuarioDao DAO para acceso a datos de usuarios.
 */
class UsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
    /**
     * Obtiene un flujo con la lista de todos los usuarios.
     *
     * @return Flujo con la lista de entidades [UsuarioEntity].
     */
    fun getUsuarios(): Flow<List<UsuarioEntity>> = usuarioDao.getUsuarios()

    /**
     * Inserta un usuario en la base de datos.
     *
     * @param usuario Entidad de usuario a insertar.
     */
    suspend fun insertUsuario(usuario: UsuarioEntity) = usuarioDao.insertUsuario(usuario)

    /**
     * Busca un usuario por correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Entidad [UsuarioEntity] si existe, null si no.
     */
    suspend fun getUserByEmail(email: String): UsuarioEntity? = usuarioDao.getUserByEmail(email)

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario Entidad de usuario a eliminar.
     */
    suspend fun deleteUsuario(usuario: UsuarioEntity) = usuarioDao.deleteUsuario(usuario)

    /**
     * Elimina un usuario por correo electrónico.
     *
     * @param email Correo electrónico del usuario a eliminar.
     */
    suspend fun deleteUserByEmail(email: String) = usuarioDao.deleteUserByEmail(email)
}

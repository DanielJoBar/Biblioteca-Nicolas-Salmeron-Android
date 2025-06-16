package com.example.bibliotecanicolassalmeron.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestionar las operaciones sobre la entidad [UsuarioEntity] en la base de datos.
 */
@Dao
interface UsuarioDao {

    /**
     * Obtiene todos los usuarios registrados como un flujo observable.
     *
     * @return Flow que emite listas de [UsuarioEntity].
     */
    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): Flow<List<UsuarioEntity>>

    /**
     * Inserta un usuario en la base de datos.
     * Si ya existe un usuario con el mismo email, será reemplazado.
     *
     * @param usuario Usuario a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param usuario Usuario con la información actualizada.
     */
    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario Usuario a eliminar.
     */
    @Delete
    suspend fun deleteUsuario(usuario: UsuarioEntity)

    /**
     * Elimina un usuario de la base de datos por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a eliminar.
     */
    @Query("DELETE FROM usuarios WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Instancia de [UsuarioEntity] si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UsuarioEntity?
}

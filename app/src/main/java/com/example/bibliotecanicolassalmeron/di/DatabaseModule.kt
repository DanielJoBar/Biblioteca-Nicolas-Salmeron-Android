package com.example.bibliotecanicolassalmeron.di

import android.content.Context
import com.example.bibliotecanicolassalmeron.data.database.BibliotecaDatabase
import com.example.bibliotecanicolassalmeron.data.database.ReservasDatabase
import com.example.bibliotecanicolassalmeron.data.database.dao.LibroDao
import com.example.bibliotecanicolassalmeron.data.database.dao.ReservaDao
import com.example.bibliotecanicolassalmeron.data.database.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Módulo Dagger Hilt para proveer instancias de bases de datos y DAOs.
 *
 * Este módulo se instala en el SingletonComponent para que sus dependencias tengan ciclo de vida global.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee la base de datos principal (solo lectura desde la API).
     *
     * @param context Contexto de la aplicación.
     * @return Instancia singleton de [BibliotecaDatabase].
     */
    @Provides
    @Singleton
    fun provideBibliotecaDatabase(@ApplicationContext context: Context): BibliotecaDatabase {
        return BibliotecaDatabase.getInstance(context)
    }

    /**
     * Provee la base de datos local para almacenamiento de reservas y datos del usuario.
     *
     * @param context Contexto de la aplicación.
     * @return Instancia singleton de [ReservasDatabase].
     */
    @Provides
    @Singleton
    fun provideReservasDatabase(@ApplicationContext context: Context): ReservasDatabase {
        return ReservasDatabase.getInstance(context)
    }

    /**
     * Provee el DAO para acceder a libros en la base de datos principal.
     *
     * @param database Instancia de [BibliotecaDatabase].
     * @return DAO de libros [LibroDao].
     */
    @Provides
    @Singleton
    fun provideLibroDao(database: BibliotecaDatabase): LibroDao = database.libroDao()

    /**
     * Provee el DAO para acceder a usuarios en la base de datos principal.
     *
     * @param database Instancia de [BibliotecaDatabase].
     * @return DAO de usuarios [UsuarioDao].
     */
    @Provides
    @Singleton
    fun provideUsuarioDao(database: BibliotecaDatabase): UsuarioDao = database.usuarioDao()

    /**
     * Provee el DAO para acceder a reservas en la base de datos local.
     *
     * @param database Instancia de [ReservasDatabase].
     * @return DAO de reservas [ReservaDao].
     */
    @Provides
    @Singleton
    fun provideReservaDao(database: ReservasDatabase): ReservaDao = database.reservaDao()
}

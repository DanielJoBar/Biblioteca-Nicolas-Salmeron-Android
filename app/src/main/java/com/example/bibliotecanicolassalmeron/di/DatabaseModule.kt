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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Proveer la base de datos principal (solo lectura desde la API)
    @Provides
    @Singleton
    fun provideBibliotecaDatabase(@ApplicationContext context: Context): BibliotecaDatabase {
        return BibliotecaDatabase.getInstance(context)
    }

    // Proveer la base de datos local (almacenamiento de reservas y datos del usuario)
    @Provides
    @Singleton
    fun provideReservasDatabase(@ApplicationContext context: Context): ReservasDatabase {
        return ReservasDatabase.getInstance(context)
    }

    // DAOs para la base de datos BibliotecaDatabase
    @Provides
    @Singleton
    fun provideLibroDao(database: BibliotecaDatabase): LibroDao = database.libroDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(database: BibliotecaDatabase): UsuarioDao = database.usuarioDao()

    // DAO para la base de datos LocalBibliotecaDatabase
    @Provides
    @Singleton
    fun provideReservaDao(database: ReservasDatabase): ReservaDao = database.reservaDao()
}

package com.example.bibliotecanicolassalmeron.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger Hilt para proveer instancias relacionadas con Firebase.
 *
 * Este módulo está instalado en el SingletonComponent para que las instancias tengan ciclo de vida global.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provee la instancia singleton de FirebaseFirestore.
     *
     * Se asume que Firebase ya está inicializado correctamente en la aplicación
     * (por ejemplo, a través del archivo google-services.json).
     *
     * @return Instancia de [FirebaseFirestore].
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}

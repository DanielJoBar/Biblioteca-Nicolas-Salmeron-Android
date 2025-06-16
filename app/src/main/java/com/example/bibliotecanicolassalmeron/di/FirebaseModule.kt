package com.example.bibliotecanicolassalmeron.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        // Se asume que Firebase ya está inicializado (p.ej. mediante google-services.json)
        return FirebaseFirestore.getInstance()
    }
}

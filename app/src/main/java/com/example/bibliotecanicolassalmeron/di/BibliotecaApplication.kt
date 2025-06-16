package com.example.bibliotecanicolassalmeron.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase de aplicación principal para inicializar Hilt en el proyecto.
 *
 * Anotada con @HiltAndroidApp para habilitar la inyección de dependencias con Dagger Hilt.
 */
@HiltAndroidApp
class BibliotecaApplication : Application() {
    /**
     * Método llamado al crear la aplicación.
     * Aquí se pueden inicializar componentes globales si fuera necesario.
     */
    override fun onCreate() {
        super.onCreate()
    }
}

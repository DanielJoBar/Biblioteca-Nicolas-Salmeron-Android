package com.example.bibliotecanicolassalmeron.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BibliotecaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

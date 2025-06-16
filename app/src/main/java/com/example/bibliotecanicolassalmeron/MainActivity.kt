package com.example.bibliotecanicolassalmeron

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal que carga el fragmento de login y registro.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carga el layout que contiene los fragments de login y registro.
        setContentView(R.layout.fragment_main)
    }
}

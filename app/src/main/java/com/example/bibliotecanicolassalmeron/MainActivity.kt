package com.example.bibliotecanicolassalmeron

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // P.ej., usar el layout que contiene los fragments para login y registro:
        setContentView(R.layout.fragment_main)
    }
}

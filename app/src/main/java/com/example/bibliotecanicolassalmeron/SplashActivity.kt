package com.example.bibliotecanicolassalmeron

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad que muestra una pantalla de bienvenida (splash screen) durante 2 segundos
 * antes de navegar a la pantalla principal (MainActivity).
 */
class SplashActivity : AppCompatActivity() {

    companion object {
        // Duración en milisegundos que se mostrará la pantalla splash
        private const val SPLASH_DELAY: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        // Retrasar la navegación a MainActivity usando un Handler y el hilo principal
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
}

package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bibliotecanicolassalmeron.R

/**
 * Fragmento que muestra la sección "Acerca de" de la aplicación.
 */
class AboutFragment : Fragment() {

    /**
     * Infla el layout correspondiente al fragmento About.
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
}

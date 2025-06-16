// HomeFragment.kt
package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento principal de inicio de la aplicación.
 *
 * Muestra la pantalla de bienvenida y obtiene el correo electrónico del usuario logueado desde la actividad principal.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var email: String? = null

    /**
     * Infla el layout correspondiente al fragmento usando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta cuando la vista fue creada.
     * Obtiene el email del usuario logueado desde la actividad padre.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val innerActivity = activity as? InnerActivity
        email = innerActivity?.loggedUserEmail
    }

    /**
     * Limpia el binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

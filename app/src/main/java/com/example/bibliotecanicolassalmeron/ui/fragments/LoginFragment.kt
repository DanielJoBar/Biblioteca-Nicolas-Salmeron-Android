package com.example.bibliotecanicolassalmeron.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import com.example.bibliotecanicolassalmeron.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragmento para gestionar el inicio de sesión del usuario.
 *
 * Permite introducir email y contraseña, validarlos y navegar a la actividad principal o registro.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    /**
     * Infla la vista del fragmento usando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura los listeners para botones y lógica de validación del login.
     *
     * Cambiado Toasts a Snackbars con strings de recursos:
     * - error_complete_fields: "Completa todos los campos"
     * - success_registered: "Inicio de sesión correcto"
     * - error_invalid_credentials: "Correo o contraseña incorrectos"
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.error_fields_required), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val usuario = usuarioRepository.getUserByEmail(email)
                if (usuario != null && usuario.password == password) {
                    val intent = Intent(requireContext(), InnerActivity::class.java).apply {
                        putExtra("email", email)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    Snackbar.make(binding.root, getString(R.string.success_login), Snackbar.LENGTH_SHORT).show()
                    startActivity(intent)
                    requireActivity().finishAffinity()

                } else {
                    Snackbar.make(binding.root, getString(R.string.error_invalid_credentials), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val navController = androidx.navigation.fragment.NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    /**
     * Limpia el binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

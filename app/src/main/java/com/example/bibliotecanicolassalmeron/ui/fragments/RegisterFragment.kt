package com.example.bibliotecanicolassalmeron.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import com.example.bibliotecanicolassalmeron.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragmento que gestiona el registro de nuevos usuarios.
 *
 * Funcionalidades:
 * - Recoge y valida los datos del formulario de registro.
 * - Comprueba si el correo ya está registrado.
 * - Inserta un nuevo usuario en la base de datos local.
 * - Redirige al usuario a la actividad principal tras el registro.
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // ViewBinding para acceder a los elementos de la vista de forma segura
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Repositorio de usuarios inyectado con Hilt para acceder a la base de datos
    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    /**
     * Infla el layout asociado al fragmento usando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la lógica del formulario de registro una vez creada la vista.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Acción del botón de registro
        binding.buttonRegister.setOnClickListener {
            // Recoger datos ingresados por el usuario
            val nombre = binding.editTextNombre.text.toString().trim()
            val apellidos = binding.editTextApellidos.text.toString().trim()
            val username = binding.editTextNombreUsuario.text.toString().trim()
            val email = binding.editTextGmail.text.toString().trim()
            val password = binding.editTextContra.text.toString().trim()
            val repetir = binding.editTextRepetirContra.text.toString().trim()

            // Verifica que ningún campo esté vacío
            if (nombre.isEmpty() || apellidos.isEmpty() || username.isEmpty() ||
                email.isEmpty() || password.isEmpty() || repetir.isEmpty()
            ) {
                Snackbar.make(binding.root, R.string.error_complete_all_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica que ambas contraseñas coincidan
            if (password != repetir) {
                Snackbar.make(binding.root, R.string.error_passwords_not_match, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lanza una corrutina para operaciones en base de datos
            lifecycleScope.launch {
                // Comprueba si ya existe un usuario con el mismo email
                val existingUser = usuarioRepository.getUserByEmail(email)
                if (existingUser != null) {
                    Snackbar.make(binding.root, R.string.error_email_exists, Snackbar.LENGTH_SHORT).show()
                    return@launch
                }

                // Crea un nuevo objeto UsuarioEntity con los datos ingresados
                val usuarioEntity = UsuarioEntity(
                    id = email,
                    name = nombre,
                    surname = apellidos,
                    username = username,
                    email = email,
                    password = password,
                    pictureLarge = "",
                    pictureSmall = "",
                    pictureThumbnail = "",
                    pictureUrl = ""
                )

                // Inserta el nuevo usuario en la base de datos
                usuarioRepository.insertUsuario(usuarioEntity)

                // Muestra mensaje de éxito
                Snackbar.make(binding.root, R.string.success_registered, Snackbar.LENGTH_SHORT).show()

                // Redirige a la actividad principal (InnerActivity) y elimina el back stack
                val intent = Intent(requireContext(), InnerActivity::class.java).apply {
                    putExtra("email", email) // Pasa el email del usuario
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
                requireActivity().finish()
            }
        }

        // Botón que redirige al fragmento de login
        val goToLoginButton = view.findViewById<Button>(R.id.btnGoToLogin)
        goToLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    /**
     * Libera el binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

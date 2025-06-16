package com.example.bibliotecanicolassalmeron.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            // Recoger valores del formulario
            val nombre = binding.editTextNombre.text.toString().trim()
            val apellidos = binding.editTextApellidos.text.toString().trim()
            val username = binding.editTextNombreUsuario.text.toString().trim()
            val email = binding.editTextGmail.text.toString().trim()
            val password = binding.editTextContra.text.toString().trim()
            val repetir = binding.editTextRepetirContra.text.toString().trim()

            if (nombre.isEmpty() || apellidos.isEmpty() || username.isEmpty() ||
                email.isEmpty() || password.isEmpty() || repetir.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != repetir) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = usuarioRepository.getUserByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(
                        requireContext(),
                        "El correo ya existe. Utiliza otro",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

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

                usuarioRepository.insertUsuario(usuarioEntity)
                Toast.makeText(requireContext(), "Registro completado", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), InnerActivity::class.java).apply {
                    putExtra("email", email)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
                requireActivity().finish()
            }
        }
        val goToLoginButton = view.findViewById<Button>(R.id.btnGoToLogin)
        goToLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

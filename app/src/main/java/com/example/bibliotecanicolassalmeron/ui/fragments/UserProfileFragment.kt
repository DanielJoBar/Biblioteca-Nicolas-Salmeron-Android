package com.example.bibliotecanicolassalmeron.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.MainActivity
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import com.example.bibliotecanicolassalmeron.databinding.FragmentUserProfileBinding
import com.example.bibliotecanicolassalmeron.ui.viewmodels.UsuarioListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener datos del usuario desde InnerActivity.
        val innerActivity = activity as? InnerActivity
        val user = innerActivity?.currentUser

        // Asigna los datos en las vistas
        binding.tvUserName.text = user?.name ?: "Nombre desconocido"
        binding.tvUserSurname.text = user?.surname ?: "Apellidos desconocidos"
        binding.tvUserEmail.text = user?.email ?: "Correo desconocido"

        // Configura el Spinner de idiomas
        val languages = listOf("Español", "English", "Русский")
        val adapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Acción para "Cerrar sesión"
        binding.cardSignOut.setOnClickListener {
            Snackbar.make(binding.root, "Cerrando sesión...", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        // Acción para "Borrar cuenta"
        binding.cardDeleteAccount.setOnClickListener {
            Snackbar.make(binding.root, "Borrando cuenta...", Snackbar.LENGTH_SHORT).show()
            user?.let { usuario ->
                usuarioViewModel.deleteUserByEmail(usuario.email,
                    onComplete = {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                    },
                    onError = { mensaje ->
                        Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                    }
                )
            } ?: run {
                Snackbar.make(binding.root, "No se encontraron datos del usuario", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
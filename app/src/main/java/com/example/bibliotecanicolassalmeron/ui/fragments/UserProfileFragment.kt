package com.example.bibliotecanicolassalmeron.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.MainActivity
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.databinding.FragmentUserProfileBinding
import com.example.bibliotecanicolassalmeron.ui.viewmodels.UsuarioListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento que muestra el perfil del usuario actual.
 *
 * Funcionalidades:
 * - Visualiza nombre, apellidos y email del usuario.
 * - Permite cerrar sesión.
 * - Permite eliminar la cuenta del usuario actual.
 */
@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    // ViewBinding para acceso seguro a los elementos de la vista
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    // ViewModel que gestiona la lógica de los usuarios
    private val usuarioViewModel: UsuarioListViewModel by viewModels()

    /**
     * Infla el layout del fragmento utilizando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Lógica principal del fragmento:
     * - Muestra datos del usuario.
     * - Configura acciones de cerrar sesión y eliminar cuenta.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtiene al usuario actual desde la actividad contenedora
        val innerActivity = activity as? InnerActivity
        val user = innerActivity?.currentUser

        // Muestra nombre, apellidos y correo en la UI o textos por defecto si es nulo
        binding.tvUserName.text = user?.name ?: getString(R.string.unknown_name)
        binding.tvUserSurname.text = user?.surname ?: getString(R.string.unknown_surname)
        binding.tvUserEmail.text = user?.email ?: getString(R.string.unknown_email)

        // Acción: cerrar sesión
        binding.cardSignOut.setOnClickListener {
            // Muestra mensaje breve y redirige a la pantalla de login (MainActivity)
            Snackbar.make(binding.root, R.string.signing_out, Snackbar.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            requireActivity().finish()
        }

        // Acción: borrar cuenta del usuario actual
        binding.cardDeleteAccount.setOnClickListener {
            Snackbar.make(binding.root, R.string.deleting_account, Snackbar.LENGTH_SHORT).show()

            // Si hay usuario logueado, intenta eliminarlo
            user?.let { usuario ->
                usuarioViewModel.deleteUserByEmail(
                    usuario.email,
                    onComplete = {
                        // Una vez borrado, redirige a login
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                        startActivity(intent)
                        requireActivity().finish()
                    },
                    onError = { mensaje ->
                        // Si hay error al eliminar, muestra un mensaje al usuario
                        Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                    }
                )
            } ?: run {
                // Si no se pudo obtener el usuario actual, muestra error
                Snackbar.make(binding.root, R.string.no_user_data_found, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Libera el binding al destruir la vista para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

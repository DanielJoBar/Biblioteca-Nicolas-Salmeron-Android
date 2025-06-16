package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.R
import com.google.android.material.snackbar.Snackbar
import com.example.bibliotecanicolassalmeron.databinding.FragmentReservationBinding
import com.example.bibliotecanicolassalmeron.ui.adapters.ReservationListAdapter
import com.example.bibliotecanicolassalmeron.ui.viewmodels.ReservaListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra la lista de reservas del usuario actual.
 *
 * - Recupera las reservas del usuario desde el ViewModel.
 * - Muestra la lista en un RecyclerView.
 * - Permite al usuario eliminar reservas con confirmación vía Snackbar.
 */
@AndroidEntryPoint
class ReservationFragment : Fragment() {

    // ViewBinding para acceder a los elementos de la vista
    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    // ViewModel inyectado con Hilt
    private val viewModel: ReservaListViewModel by viewModels()

    // Adaptador para mostrar la lista de reservas
    private lateinit var adapter: ReservationListAdapter

    /**
     * Infla la vista del fragmento con ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la UI, incluyendo RecyclerView y escucha del flujo de datos.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el adaptador con una lambda para manejar el evento de eliminación
        adapter = ReservationListAdapter(onDeleteClick = { reserva ->
            viewModel.deleteReserva(reserva)
            Snackbar.make(binding.root, R.string.reservation_deleted, Snackbar.LENGTH_SHORT).show()
        })

        // Configura el RecyclerView con el adaptador y el layout manager
        binding.recyclerViewReservation.adapter = adapter
        binding.recyclerViewReservation.layoutManager = LinearLayoutManager(requireContext())

        // Obtiene el ID del usuario actual desde la actividad contenedora
        val userLocalId = (activity as? InnerActivity)?.currentUser?.localId
        userLocalId?.let {
            // Carga las reservas del usuario mediante el ViewModel
            viewModel.loadReservasByUser(it)
        }

        // Observa el flujo de reservas desde el ViewModel mientras el ciclo de vida esté activo
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reservas.collect { reservaList ->
                    adapter.submitList(reservaList) // Actualiza la lista en el adaptador
                }
            }
        }
    }

    /**
     * Libera el binding cuando la vista es destruida para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

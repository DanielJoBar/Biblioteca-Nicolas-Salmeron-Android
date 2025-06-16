package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import com.example.bibliotecanicolassalmeron.databinding.FragmentReservationBinding
import com.example.bibliotecanicolassalmeron.ui.adapters.ReservationListAdapter
import com.example.bibliotecanicolassalmeron.ui.viewmodels.ReservaListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReservaListViewModel by viewModels()
    private lateinit var adapter: ReservationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReservationListAdapter(onDeleteClick = { reserva ->
            viewModel.deleteReserva(reserva) {
                Snackbar.make(binding.root, "Reserva eliminada", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.recyclerViewReservation.adapter = adapter
        binding.recyclerViewReservation.layoutManager = LinearLayoutManager(requireContext())

        val userLocalId = (activity as? InnerActivity)?.currentUser?.localId
        userLocalId?.let {
            viewModel.loadReservasByUser(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reservas.collect { reservaList ->
                    adapter.submitList(reservaList)
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

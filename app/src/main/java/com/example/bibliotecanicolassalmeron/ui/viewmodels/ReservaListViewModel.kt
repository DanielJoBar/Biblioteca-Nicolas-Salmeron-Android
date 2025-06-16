package com.example.bibliotecanicolassalmeron.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.data.model.Reserva
import com.example.bibliotecanicolassalmeron.data.repository.ReservaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservaListViewModel @Inject constructor(
    private val reservaRepository: ReservaRepository
) : ViewModel() {

    // ID del usuario actual
    private val currentUserLocalId = MutableStateFlow<Int?>(null)

    val reservas: StateFlow<List<Reserva>> = currentUserLocalId
        .flatMapLatest { userLocalId ->
            if (userLocalId == null) {
                flowOf(emptyList())
            } else {
                reservaRepository.getReservasByUserId(userLocalId.toString())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun loadReservasByUser(userLocalId: Int) {
        currentUserLocalId.value = userLocalId
    }

    fun insertReserva(reserva: Reserva, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val exists = reservaRepository.existsReservation(reserva.bookId, reserva.userId)
            if (exists) {
                onError("Ya tienes una reserva para \"${reserva.titulo}\"")
            } else {
                reservaRepository.insertReserva(reserva)
                onComplete()
            }
        }
    }


    fun deleteReserva(reserva: Reserva, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            reservaRepository.deleteReserva(reserva)
            onComplete()
        }
    }
}

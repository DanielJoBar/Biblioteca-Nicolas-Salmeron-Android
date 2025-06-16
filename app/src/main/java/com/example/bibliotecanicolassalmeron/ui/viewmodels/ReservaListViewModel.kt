package com.example.bibliotecanicolassalmeron.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecanicolassalmeron.data.model.Reserva
import com.example.bibliotecanicolassalmeron.data.repository.ReservaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Define a sealed class to represent different UI events.
 * This makes it clear what kinds of messages can be sent to the UI.
 */
sealed class ReservationEvent {
    data object ReservationAddedSuccess : ReservationEvent()
    data class ReservationAlreadyExists(val bookTitle: String) : ReservationEvent()
    data object ReservationDeletedSuccess : ReservationEvent()
    data object GeneralError : ReservationEvent() // For other potential errors
}

/**
 * ViewModel para gestionar las reservas del usuario.
 *
 * Mantiene un flujo de reservas filtradas por el usuario actual.
 *
 * @property reservaRepository Repositorio para acceso a datos de reservas.
 */
@HiltViewModel
class ReservaListViewModel @Inject constructor(
    private val reservaRepository: ReservaRepository
) : ViewModel() {

    /** ID local del usuario actual */
    private val currentUserLocalId = MutableStateFlow<Int?>(null)

    /** StateFlow con la lista de reservas filtradas por usuario */
    val reservas: StateFlow<List<Reserva>> = currentUserLocalId
        .flatMapLatest { userLocalId ->
            if (userLocalId == null) {
                flowOf(emptyList())
            } else {
                reservaRepository.getReservasByUserId(userLocalId.toString())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * SharedFlow para emitir eventos de UI (como mensajes de Snackbar).
     * Collectors will receive events that are emitted after they start collecting.
     */
    private val _eventFlow = MutableSharedFlow<ReservationEvent>()
    val eventFlow: SharedFlow<ReservationEvent> = _eventFlow

    /**
     * Carga las reservas filtradas para un usuario dado.
     *
     * @param userLocalId ID local del usuario.
     */
    fun loadReservasByUser(userLocalId: Int) {
        currentUserLocalId.value = userLocalId
    }

    /**
     * Inserta una reserva si no existe ya una para ese libro y usuario.
     * Emite eventos a la UI para mostrar Snackbars.
     *
     * @param reserva Reserva a insertar.
     */
    fun insertReserva(reserva: Reserva) {
        viewModelScope.launch {
            try {
                val exists = reservaRepository.existsReservation(reserva.bookId, reserva.userId)
                if (exists) {
                    _eventFlow.emit(ReservationEvent.ReservationAlreadyExists(reserva.titulo))
                } else {
                    reservaRepository.insertReserva(reserva)
                    _eventFlow.emit(ReservationEvent.ReservationAddedSuccess)
                }
            } catch (e: Exception) {
                _eventFlow.emit(ReservationEvent.GeneralError)
            }
        }
    }

    /**
     * Elimina una reserva.
     * Emite eventos a la UI para mostrar Snackbars.
     *
     * @param reserva Reserva a eliminar.
     */
    fun deleteReserva(reserva: Reserva) {
        viewModelScope.launch {
            try {
                reservaRepository.deleteReserva(reserva)
                _eventFlow.emit(ReservationEvent.ReservationDeletedSuccess)
            } catch (e: Exception) {
                _eventFlow.emit(ReservationEvent.GeneralError)
            }
        }
    }
}
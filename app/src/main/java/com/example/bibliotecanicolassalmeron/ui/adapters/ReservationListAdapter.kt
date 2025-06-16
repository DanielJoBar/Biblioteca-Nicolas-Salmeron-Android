package com.example.bibliotecanicolassalmeron.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bibliotecanicolassalmeron.data.model.Reserva
import com.example.bibliotecanicolassalmeron.databinding.ItemReservationBinding

/**
 * Adaptador para mostrar una lista de reservas en un RecyclerView.
 *
 * @property onDeleteClick Callback que se ejecuta al pulsar el botón eliminar en una reserva.
 */
class ReservationListAdapter(
    private val onDeleteClick: (Reserva) -> Unit
) : ListAdapter<Reserva, ReservationListAdapter.ReservationViewHolder>(ReservationDiffCallback) {

    /**
     * ViewHolder que representa un ítem de reserva.
     *
     * @param binding Enlace a la vista del ítem.
     */
    inner class ReservationViewHolder(private val binding: ItemReservationBinding)
        : RecyclerView.ViewHolder(binding.root) {

        /**
         * Asocia los datos de la reserva a la vista.
         *
         * @param reserva Reserva que se mostrará en el ítem.
         */
        fun bind(reserva: Reserva) {
            binding.tvReservationTitle.text = reserva.titulo
            binding.btnDeleteReservation.setOnClickListener {
                onDeleteClick(reserva)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Callback para optimizar las actualizaciones en la lista de reservas.
     */
    object ReservationDiffCallback : DiffUtil.ItemCallback<Reserva>() {
        override fun areItemsTheSame(oldItem: Reserva, newItem: Reserva): Boolean {
            return oldItem.localId == newItem.localId && oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: Reserva, newItem: Reserva): Boolean {
            return oldItem == newItem
        }
    }
}

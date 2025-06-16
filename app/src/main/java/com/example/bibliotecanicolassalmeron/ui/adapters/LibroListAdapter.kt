package com.example.bibliotecanicolassalmeron.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.databinding.ItemBookBinding

/**
 * Adaptador para mostrar una lista de libros en un RecyclerView.
 *
 * @property onItemClick Callback cuando se hace clic en un libro.
 * @property onDeleteClick Callback cuando se hace clic en el botón de eliminar de un libro.
 */
class LibroListAdapter(
    private val onItemClick: (Libro) -> Unit,
    private val onDeleteClick: (Libro) -> Unit
) : ListAdapter<Libro, LibroListAdapter.LibroViewHolder>(BookDiffCallback) {

    /**
     * ViewHolder para un ítem de libro.
     *
     * @param binding Enlace a la vista del ítem.
     */
    inner class LibroViewHolder(private val binding: ItemBookBinding)
        : RecyclerView.ViewHolder(binding.root) {

        /**
         * Asocia los datos del libro a la vista.
         *
         * @param libro Libro que se mostrará en el ítem.
         */
        fun bind(libro: Libro) {
            binding.bookTitle.text = libro.titulo
            binding.bookCover.load(libro.imagen) {
                crossfade(true)
            }
            binding.root.setOnClickListener {
                onItemClick(libro)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClick(libro)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LibroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Callback para optimizar los cambios en la lista de libros.
     */
    object BookDiffCallback: DiffUtil.ItemCallback<Libro>() {
        override fun areItemsTheSame(oldItem: Libro, newItem: Libro): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Libro, newItem: Libro): Boolean =
            oldItem == newItem
    }
}

package com.example.bibliotecanicolassalmeron.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.databinding.ItemBookBinding


class LibroListAdapter(
    private val onItemClick: (Libro) -> Unit,
    private val onDeleteClick: (Libro) -> Unit
) : ListAdapter<Libro, LibroListAdapter.LibroViewHolder>(BookDiffCallback) {

    inner class LibroViewHolder(private val binding: ItemBookBinding)
        : RecyclerView.ViewHolder(binding.root) {
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

    object BookDiffCallback: DiffUtil.ItemCallback<Libro>() {
        override fun areItemsTheSame(oldItem: Libro, newItem: Libro): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Libro, newItem: Libro): Boolean =
            oldItem == newItem
    }
}

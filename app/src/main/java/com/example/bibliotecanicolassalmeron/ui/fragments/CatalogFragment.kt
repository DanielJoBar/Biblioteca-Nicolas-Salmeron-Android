// CatalogFragment.kt
package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.databinding.FragmentCatalogBinding
import com.example.bibliotecanicolassalmeron.ui.adapters.LibroListAdapter
import com.example.bibliotecanicolassalmeron.ui.viewmodels.LibroListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra el catálogo de libros.
 *
 * Permite buscar libros, navegar a detalles, añadir nuevos y eliminar libros.
 */
@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LibroListViewModel by viewModels()
    private lateinit var adapter: LibroListAdapter
    private var fullLibroList: List<Libro> = emptyList()

    /**
     * Infla la vista del fragmento usando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la UI después de que la vista ha sido creada.
     * Configura RecyclerView, búsqueda y listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibroListAdapter(
            onItemClick = { libro ->
                val action = CatalogFragmentDirections.actionCatalogFragmentToLibroDetailFragment(book = libro)
                findNavController().navigate(action)
            },
            onDeleteClick = { libro ->
                viewModel.deleteLibro(libro)
                // Reemplazado Toast por Snackbar con texto de recursos strings.xml
                Snackbar.make(binding.root, getString(R.string.libro_eliminado), Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewCatalog.adapter = adapter
        binding.recyclerViewCatalog.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable?.toString()?.trim() ?: ""
            if (query.isEmpty()) {
                adapter.submitList(fullLibroList)
            } else {
                adapter.submitList(fullLibroList.filter { it.titulo.contains(query, ignoreCase = true) })
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.libros.collect { libroList ->
                    fullLibroList = libroList
                    val query = binding.searchEditText.text?.toString()?.trim() ?: ""
                    if (query.isEmpty()) {
                        adapter.submitList(fullLibroList)
                    } else {
                        adapter.submitList(fullLibroList.filter { it.titulo.contains(query, ignoreCase = true) })
                    }
                }
            }
        }

        binding.formButton.setOnClickListener {
            val action = CatalogFragmentDirections.actionCatalogFragmentToBookFormFragment(mode = "create", book = null)
            findNavController().navigate(action)
        }
    }

    /**
     * Limpia el binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

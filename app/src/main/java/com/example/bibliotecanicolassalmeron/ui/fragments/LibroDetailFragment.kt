package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.R
import com.google.android.material.snackbar.Snackbar
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.data.model.Reserva
import com.example.bibliotecanicolassalmeron.databinding.FragmentBookDetailBinding
import com.example.bibliotecanicolassalmeron.ui.viewmodels.ReservaListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LibroDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!
    private val reservaViewModel: ReservaListViewModel by viewModels()
    private var currentLibro: Libro? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genreOptions = resources.getStringArray(R.array.genre_options)
        val genreAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genreOptions)

        binding.autoCompleteGenre.setAdapter(genreAdapter)
        currentLibro = LibroDetailFragmentArgs.fromBundle(requireArguments()).book
        currentLibro?.let { libro ->
            val uri = libro.imagen.takeIf(String::isNotBlank)
                ?: "android.resource://${requireContext().packageName}/${R.drawable.no_image}"
            binding.ivBookCover.load(uri) {
                placeholder(R.drawable.no_image)
                error(R.drawable.no_image)
                crossfade(true)
            }
            binding.tvTitle.text = libro.titulo
            binding.tvIsbn.text = libro.isbn
            binding.tvAuthor.text = libro.author
            binding.tvSummary.text = libro.resumen
        }
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Libro>("updatedBook")
            ?.observe(viewLifecycleOwner) { updatedBook ->
                currentLibro = updatedBook
                binding.tvTitle.text = updatedBook.titulo
                binding.autoCompleteGenre.setText(updatedBook.genero, false)
                binding.tvIsbn.text = updatedBook.isbn
                binding.tvAuthor.text = updatedBook.author
                binding.tvSummary.text = updatedBook.resumen
            }

        // Configurar el botón de editar (ya existente)
        binding.btnEdit.setOnClickListener {
            currentLibro?.let { libro ->
                val action = LibroDetailFragmentDirections.actionLibroDetailFragmentToBookFormFragment(
                    mode = "edit",
                    book = libro
                )
                findNavController().navigate(action)
            }
        }

        binding.btnBook.setOnClickListener {
            currentLibro?.let { libro ->
                val innerActivity = requireActivity() as? InnerActivity
                val currentUser = innerActivity?.currentUser

                if (currentUser == null) {
                    Snackbar.make(binding.root, "No se pudo obtener los datos del usuario", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val userId = currentUser.localId

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val reservationDate = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val expirationDate = dateFormat.format(calendar.time)

                val newReserva = Reserva(
                    bookId = libro.id.toString(),
                    titulo = libro.titulo,
                    reservationDate = reservationDate,
                    expirationDate = expirationDate,
                    userId = userId.toString()
                )

                reservaViewModel.insertReserva(
                    reserva = newReserva,
                    onComplete = {
                        Snackbar.make(binding.root, "Reserva creada para \"${libro.titulo}\"", Snackbar.LENGTH_SHORT).show()
                    },
                    onError = { errorMsg ->
                        Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_SHORT).show()
                    }
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

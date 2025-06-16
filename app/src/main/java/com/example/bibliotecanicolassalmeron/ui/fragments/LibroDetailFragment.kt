package com.example.bibliotecanicolassalmeron.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * Fragmento que muestra los detalles de un libro seleccionado.
 * Permite al usuario:
 * - Ver información del libro
 * - Editar el libro si es necesario
 * - Reservar el libro para su usuario actual
 */
@AndroidEntryPoint
class LibroDetailFragment : Fragment() {

    // ViewBinding para acceso a los elementos de la vista
    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel que gestiona las reservas
    private val reservaViewModel: ReservaListViewModel by viewModels()

    // Libro actual mostrado en pantalla
    private var currentLibro: Libro? = null

    /**
     * Infla la vista del fragmento con ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la lógica de UI: muestra datos, permite edición y reserva del libro.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera el libro pasado como argumento desde el fragmento anterior
        currentLibro = LibroDetailFragmentArgs.fromBundle(requireArguments()).book

        // Si el libro existe, muestra sus datos en la interfaz
        currentLibro?.let { libro ->
            val uri = libro.imagen.takeIf(String::isNotBlank)
                ?: "android.resource://${requireContext().packageName}/${R.drawable.no_image}"

            // Carga imagen del libro usando Coil
            binding.ivBookCover.load(uri) {
                placeholder(R.drawable.no_image)
                error(R.drawable.no_image)
                crossfade(true)
            }

            // Muestra otros campos del libro
            binding.tvGenre.text = libro.genero
            binding.tvTitle.text = libro.titulo
            binding.tvIsbn.text = libro.isbn
            binding.tvAuthor.text = libro.author
            binding.tvSummary.text = libro.resumen
        }

        // Observa si el libro fue actualizado desde el formulario y actualiza UI
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Libro>("updatedBook")
            ?.observe(viewLifecycleOwner) { updatedBook ->
                currentLibro = updatedBook
                binding.tvTitle.text = updatedBook.titulo
                binding.tvGenre.text = updatedBook.genero
                binding.tvIsbn.text = updatedBook.isbn
                binding.tvAuthor.text = updatedBook.author
                binding.tvSummary.text = updatedBook.resumen
            }

        // Botón que navega al formulario de edición de libros
        binding.btnEdit.setOnClickListener {
            currentLibro?.let { libro ->
                val action = LibroDetailFragmentDirections.actionLibroDetailFragmentToBookFormFragment(
                    mode = "edit",
                    book = libro
                )
                findNavController().navigate(action)
            }
        }

        // Botón que crea una reserva del libro para el usuario actual
        binding.btnBook.setOnClickListener {
            currentLibro?.let { libro ->
                val innerActivity = requireActivity() as? InnerActivity
                val currentUser = innerActivity?.currentUser

                // Si no se encuentra el usuario, mostrar error
                if (currentUser == null) {
                    Snackbar.make(binding.root, getString(R.string.error_no_user_data), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val userId = currentUser.localId

                // Calcula la fecha de reserva (hoy) y de expiración (+7 días)
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val reservationDate = dateFormat.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val expirationDate = dateFormat.format(calendar.time)

                // Crea el objeto Reserva y lo envía al ViewModel
                val newReserva = Reserva(
                    bookId = libro.id.toString(),
                    titulo = libro.titulo,
                    reservationDate = reservationDate,
                    expirationDate = expirationDate,
                    userId = userId.toString()
                )

                reservaViewModel.insertReserva(newReserva)
                Snackbar.make(binding.root, getString(R.string.reserva_exitosa), Snackbar.LENGTH_SHORT).show()
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

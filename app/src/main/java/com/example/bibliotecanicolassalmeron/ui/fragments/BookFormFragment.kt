// Paquete y dependencias necesarias
package com.example.bibliotecanicolassalmeron.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.databinding.FragmentBookFormBinding
import com.example.bibliotecanicolassalmeron.ui.viewmodels.LibroListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

/**
 * Fragmento responsable de la creación y edición de libros.
 * Gestiona entrada de datos del usuario, imagen del libro, permisos de galería/cámara,
 * y persistencia de datos a través del ViewModel.
 */
@AndroidEntryPoint
class BookFormFragment : Fragment() {

    // ViewBinding para acceder a las vistas
    private var _binding: FragmentBookFormBinding? = null
    private val binding get() = _binding!!

    // ViewModel para operar sobre los libros
    private val viewModel: LibroListViewModel by viewModels()

    // URI de la imagen seleccionada
    private var selectedImageUri: Uri? = null

    /**
     * Launcher que abre la galería para seleccionar una imagen.
     * Al recibir la URI, se actualiza la imagen del libro y se guarda como tag.
     */
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.ivBookImage.setImageURI(it)
            binding.ivBookImage.tag = it.toString()
        }
    }

    /**
     * Launcher para solicitar el permiso necesario de acceso a galería.
     * Si se concede, se abre la galería; si no, se muestra un mensaje.
     */
    private val requestGalleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Snackbar.make(binding.root, getString(R.string.permission_gallery_denied), Snackbar.LENGTH_SHORT).show()
        }
    }

    // Variables que controlan si estamos en modo edición o creación, y el libro a editar
    private lateinit var mode: String
    private var bookToEdit: Libro? = null

    /**
     * Recupera los argumentos del fragmento (modo y libro si corresponde)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = BookFormFragmentArgs.fromBundle(requireArguments())
        mode = args.mode
        if (mode == "edit") {
            bookToEdit = args.book
        }
    }

    /**
     * Infla el layout del fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la lógica de la UI:
     * - Listado de géneros
     * - Menú de imagen (galería, cámara, eliminar)
     * - Carga de datos en modo edición
     * - Guardado del libro (creación o actualización)
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del autocompletado de géneros
        val genreOptions = resources.getStringArray(R.array.genre_options)
        val genreAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genreOptions)
        binding.autoCompleteGenre.setAdapter(genreAdapter)

        // Listener para abrir la galería
        binding.optionGallery.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
            checkGalleryPermission()
        }

        // Navega al fragmento de cámara para tomar foto
        binding.optionCamera.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
            findNavController().navigate(R.id.action_bookFormFragment_to_cameraFragment)
        }

        // Elimina la imagen seleccionada
        binding.optionRemove.setOnClickListener {
            selectedImageUri = null
            binding.ivBookImage.setImageResource(R.drawable.no_image)
            binding.layoutMiniMenu.visibility = View.GONE
        }

        // Observa datos de retorno desde CameraFragment (imagen tomada)
        val navBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = navBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>("cameraImageUri")?.observe(viewLifecycleOwner) { uriString ->
            uriString?.let {
                val uri = Uri.parse(it)
                binding.ivBookImage.setImageURI(uri)
                binding.ivBookImage.tag = uri.toString()
            }
        }

        // Si estamos en modo edición, rellenar campos con los datos del libro
        if (mode == "edit" && bookToEdit != null) {
            val uri = when {
                bookToEdit!!.imagen.startsWith("content://") || bookToEdit!!.imagen.startsWith("file://") -> bookToEdit!!.imagen
                bookToEdit!!.imagen.isNotBlank() -> "file://${bookToEdit!!.imagen}"
                else -> "android.resource://${requireContext().packageName}/${R.drawable.no_image}"
            }
            binding.ivBookImage.load(uri) {
                placeholder(R.drawable.no_image)
                error(R.drawable.no_image)
                crossfade(true)
            }
            binding.ivBookImage.tag = bookToEdit!!.imagen
            binding.etTitle.setText(bookToEdit!!.titulo)
            binding.autoCompleteGenre.setText(bookToEdit!!.genero, false)
            binding.etIsbn.setText(bookToEdit!!.isbn)
            binding.etAuthor.setText(bookToEdit!!.author)
            binding.etSummary.setText(bookToEdit!!.resumen)
        } else {
            // Si no hay imagen (modo creación), se usa imagen por defecto
            binding.ivBookImage.setImageResource(R.drawable.no_image)
        }

        // Muestra el menú flotante al tocar la imagen
        binding.ivBookImage.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.VISIBLE
        }

        // Oculta el menú flotante si se hace clic fuera
        binding.layoutMiniMenu.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
        }

        // Lógica de guardado al hacer clic en "Guardar"
        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val genre = binding.autoCompleteGenre.text.toString().trim()
            val isbn = binding.etIsbn.text.toString().trim()
            val author = binding.etAuthor.text.toString().trim()
            val summary = binding.etSummary.text.toString().trim()

            // Validación de campos obligatorios
            if (title.isEmpty() || genre.isEmpty() || isbn.isEmpty() || author.isEmpty() || summary.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.error_fields_required), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Imagen: usa la seleccionada o la existente o una por defecto
            val defaultImageUri = "android.resource://${requireContext().packageName}/${R.drawable.no_image}"
            val imagen = when {
                binding.ivBookImage.tag != null -> binding.ivBookImage.tag as String
                mode == "edit" && bookToEdit != null -> bookToEdit!!.imagen
                else -> defaultImageUri
            }

            // Construcción del nuevo libro
            val newBook = Libro(
                id = if (mode == "edit" && bookToEdit != null) bookToEdit!!.id else 0,
                titulo = title,
                genero = genre,
                isbn = isbn,
                author = author,
                imagen = imagen,
                resumen = summary
            )

            // Inserta o actualiza el libro desde una corrutina
            lifecycleScope.launch {
                if (mode == "edit") {
                    viewModel.updateLibro(newBook) {
                        Snackbar.make(binding.root, getString(R.string.book_updated), Snackbar.LENGTH_SHORT).show()
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("updatedBook", newBook)
                        findNavController().navigateUp()
                    }
                } else {
                    viewModel.insertLibro(newBook) {
                        Snackbar.make(binding.root, getString(R.string.book_created), Snackbar.LENGTH_SHORT).show()
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("updatedBook", newBook)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    /**
     * Limpia la referencia del binding al destruir la vista
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Abre la galería para seleccionar una imagen
     */
    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    /**
     * Comprueba si se tiene permiso para acceder a la galería.
     * Si no, lo solicita. Adapta el permiso según la versión de Android.
     */
    private fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Snackbar.make(binding.root, getString(R.string.permission_gallery_needed), Snackbar.LENGTH_LONG).show()
            }
            else -> {
                requestGalleryPermission.launch(permission)
            }
        }
    }
}

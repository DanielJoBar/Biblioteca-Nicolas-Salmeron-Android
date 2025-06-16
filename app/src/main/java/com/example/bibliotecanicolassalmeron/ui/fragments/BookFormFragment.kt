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
import com.example.bibliotecanicolassalmeron.InnerActivity
import com.example.bibliotecanicolassalmeron.R
import com.example.bibliotecanicolassalmeron.data.model.Libro
import com.example.bibliotecanicolassalmeron.databinding.FragmentBookFormBinding
import com.example.bibliotecanicolassalmeron.ui.viewmodels.LibroListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BookFormFragment : Fragment() {

    private var _binding: FragmentBookFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LibroListViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.ivBookImage.setImageURI(it)
            binding.ivBookImage.tag = it.toString()
        }
    }

    private val requestGalleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Snackbar.make(binding.root, "Permiso denegado para acceder a la galería", Snackbar.LENGTH_SHORT).show()
        }
    }

    private lateinit var mode: String
    private var bookToEdit: Libro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = BookFormFragmentArgs.fromBundle(requireArguments())
        mode = args.mode
        if (mode == "edit") {
            bookToEdit = args.book
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el menú desplegable para el género
        val genreOptions = resources.getStringArray(R.array.genre_options)
        val genreAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genreOptions)
        binding.autoCompleteGenre.setAdapter(genreAdapter)

        binding.optionGallery.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
            checkGalleryPermission()
        }

        binding.optionCamera.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
            findNavController().navigate(R.id.action_bookFormFragment_to_cameraFragment)
        }

        binding.optionRemove.setOnClickListener {
            selectedImageUri = null
            binding.ivBookImage.setImageResource(R.drawable.no_image)
            binding.layoutMiniMenu.visibility = View.GONE
        }

        val navBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = navBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>("cameraImageUri")?.observe(viewLifecycleOwner) { uriString ->
            uriString?.let {
                val uri = Uri.parse(it)
                binding.ivBookImage.setImageURI(uri)
                binding.ivBookImage.tag = uri.toString()
            }
        }

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("cameraImageUri")
            ?.observe(viewLifecycleOwner) { uriString ->
                uriString?.let {
                    val uri = Uri.parse(it)
                    binding.ivBookImage.setImageURI(uri)
                    binding.ivBookImage.tag = it
                }
            }

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
            // Pre-poblar los campos con la información del libro
            binding.etTitle.setText(bookToEdit!!.titulo)
            binding.autoCompleteGenre.setText(bookToEdit!!.genero, false)
            binding.etIsbn.setText(bookToEdit!!.isbn)
            binding.etAuthor.setText(bookToEdit!!.author)
            binding.etSummary.setText(bookToEdit!!.resumen)
        } else {
            binding.ivBookImage.setImageResource(R.drawable.no_image)
        }

        binding.ivBookImage.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.VISIBLE
        }

        binding.layoutMiniMenu.setOnClickListener {
            binding.layoutMiniMenu.visibility = View.GONE
        }

        binding.btnSubmit.setOnClickListener {
            Log.d("BookFormFragment", "Enviando libro: $it")
            val title = binding.etTitle.text.toString().trim()
            val genre = binding.autoCompleteGenre.text.toString().trim()
            val isbn = binding.etIsbn.text.toString().trim()
            val author = binding.etAuthor.text.toString().trim()
            val summary = binding.etSummary.text.toString().trim()

            if (title.isEmpty() || genre.isEmpty() || isbn.isEmpty() || author.isEmpty() || summary.isEmpty()) {
                Snackbar.make(binding.root, "Todos los campos deben estar rellenos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val defaultImageUri = "android.resource://${requireContext().packageName}/${R.drawable.no_image}"
            val imagen = when {
                binding.ivBookImage.tag != null -> binding.ivBookImage.tag as String
                mode == "edit" && bookToEdit != null -> bookToEdit!!.imagen
                else -> defaultImageUri
            }
            val newBook = Libro(
                id = if (mode == "edit" && bookToEdit != null) bookToEdit!!.id else 0,
                titulo = title,
                genero = genre,
                isbn = isbn,
                author = author,
                imagen = imagen,
                resumen = summary
            )

            lifecycleScope.launch {
                if (mode == "edit") {
                    viewModel.updateLibro(newBook) {
                        Snackbar.make(binding.root, "Libro actualizado correctamente", Snackbar.LENGTH_SHORT).show()
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("updatedBook", newBook)
                        Log.d("BookFormFragment", "Libro actualizado, navegando hacia atrás")
                        findNavController().navigateUp()
                    }
                } else {
                    viewModel.insertLibro(newBook) {
                        Snackbar.make(binding.root, "Libro creado correctamente", Snackbar.LENGTH_SHORT).show()
                        Log.d("BookFormFragment", "Libro insertado, navegando hacia atrás")
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("newBook", newBook)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

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
                Snackbar.make(binding.root, "Se necesita permiso para acceder a la galería", Snackbar.LENGTH_LONG).show()
            }
            else -> {
                requestGalleryPermission.launch(permission)
            }
        }
    }
}

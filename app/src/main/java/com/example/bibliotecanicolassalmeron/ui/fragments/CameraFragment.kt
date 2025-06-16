package com.example.bibliotecanicolassalmeron.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bibliotecanicolassalmeron.databinding.FragmentCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.Preview

/**
 * Fragmento para gestionar la cámara y capturar imágenes.
 *
 * - Solicita permiso para usar la cámara si no está concedido.
 * - Inicializa la cámara y muestra la vista previa.
 * - Captura una imagen y la guarda en almacenamiento externo privado.
 * - Devuelve la URI de la imagen capturada al fragmento anterior.
 */
class CameraFragment : Fragment() {

    // Binding generado para la vista del fragmento
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    // Variable para captura de imagen
    private var imageCapture: ImageCapture? = null

    // Directorio donde se almacenan las imágenes capturadas
    private lateinit var outputDirectory: File

    // Executor para operaciones en segundo plano relacionadas con la cámara
    private lateinit var cameraExecutor: ExecutorService

    /**
     * Infla el layout del fragmento usando ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la cámara si se tiene permiso,
     * o solicita permiso si no se tiene.
     * Inicializa eventos de botones y recursos.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Comprobar si el permiso de cámara está concedido
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            // Solicitar permiso para cámara
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }

        // Configurar botón para tomar fotografía
        binding.btnTakePicture.setOnClickListener {
            takePhoto()
        }

        // Establecer directorio para guardar fotos
        outputDirectory = requireContext().getExternalFilesDir(null)!!

        // Inicializar executor para operaciones de cámara
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /**
     * Inicializa la cámara con vista previa y configuraciones necesarias.
     * Usa CameraX para obtener el proveedor y enlazar el ciclo de vida.
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraFragment", "Error al iniciar la cámara", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * Captura la foto, la guarda en archivo y envía la URI al fragmento anterior.
     */
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                /**
                 * Callback cuando la imagen se ha guardado correctamente.
                 * Se pasa la URI al fragmento anterior mediante savedStateHandle.
                 */
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    findNavController().previousBackStackEntry
                        ?.savedStateHandle?.set("cameraImageUri", savedUri.toString())
                    findNavController().popBackStack()
                }

                /**
                 * Callback en caso de error durante la captura de la imagen.
                 */
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Error al capturar la imagen: ${exception.message}", exception)
                }
            }
        )
    }

    /**
     * Limpia el binding y apaga el executor para liberar recursos al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }

    /**
     * Gestiona la respuesta a la solicitud de permisos.
     * Si se concede, inicia la cámara; si no, registra el error.
     *
     * @param requestCode Código de la petición de permiso.
     * @param permissions Lista de permisos solicitados.
     * @param grantResults Resultados de la petición de permisos.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Log.e("CameraFragment", "Permiso de cámara denegado.")
        }
    }
}

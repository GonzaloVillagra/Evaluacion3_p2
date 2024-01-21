package com.example.evaluacion3.funciones


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import com.example.evaluacion3.R.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

class camara : AppCompatActivity() {
    private lateinit var vprevia: Preview
    private lateinit var capturaImagen: ImageCapture
    private lateinit var ejecutorCamara: Executor
    private var imgCedulaDelantera: String? = null
    private var imgCedulaTrasera: String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.vista_camara)

        val pvCamara: PreviewView = findViewById(id.pvCamara)

        //codigo variables y configuraciones camX

        vprevia = Preview.Builder().build()
        capturaImagen = ImageCapture.Builder().build()
        ejecutorCamara = Executors.newSingleThreadExecutor()

        camaraInicio()
        }

    private fun camaraInicio(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            //codigo configuraciones generales de la camara, como direcciontorio de salida, formato fecha y en este cado nomvre
            cameraProvider.unbindAll()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                val dirSalida = getOutputDirectory()
                val fFecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val nArchivo = "${fFecha.format(Date())}.jpg"
                val outputFile = File(dirSalida, nArchivo)

                val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

                capturaImagen.takePicture(
                    outputOptions, ContextCompat.getMainExecutor(this),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(error: ImageCaptureException) {
                            Log.e("Camara", "Error al capturar imagen: ${error.message}", error)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri
                            if (savedUri != null){
                                val tipoFoto = intent.getStringExtra("tipoFoto")
                                if (tipoFoto == "fotoTrasera") {
                                    imgCedulaDelantera = savedUri.toString()
                                } else if (tipoFoto == "fotoDelantera") {
                                    imgCedulaTrasera = savedUri.toString()
                                }
                            }else {
                                Log.e("Camara", "La URI de la imagen guardada es nula.")
                            }
                        }
                    }
                )

                cameraProvider.bindToLifecycle(this, cameraSelector, vprevia, capturaImagen)
            } catch (exc: Exception){
                Log.e("Camara", "No se pudo vincular el caso de uso de las camara", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
//codigo para guardar fotografia

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }
}
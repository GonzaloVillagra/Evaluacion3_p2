package com.example.evaluacion3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.evaluacion3.entity.nuevacuenta
import com.example.evaluacion3.funciones.ubicacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CuentaActivity : AppCompatActivity () {

    //Declaraciones de variables

    private var nombreCET:EditText? = null
    private var rutET:EditText? = null
    private var fechaNacimientoET: EditText? = null
    private var telefonoET: EditText? = null
    private var emailET: EditText? = null
    private var solicitudbtn: Button? = null
    private var camaraDbtn: Button? = null
    private var camaraTbtn: Button? = null
    private var tFoto : String? = null
    private var imgCedulaF: String? = null
    private var imgCedulaT: String? = null
    private val fechaActual = obtenerFechaActual()
    private val obUbicacion = ubicacion()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paginasolicitud)

        //enlace con vistas XML
        nombreCET = findViewById(R.id.nombreCET)
        rutET = findViewById(R.id.RUTTE4)
        fechaNacimientoET = findViewById(R.id.fechaNacimientoET)
        telefonoET = findViewById(R.id.telefonoET)
        emailET = findViewById(R.id.emailET)
        solicitudbtn = findViewById(R.id.solicitudbtn)
        camaraDbtn = findViewById(R.id.camaraDbtn)
        camaraTbtn = findViewById(R.id.camaraTbtn)


//boton Solicitud cuenta

        solicitudbtn?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                obtenerUbicacion()
            } else {
                pPermiso.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

//boton camara delantera llamado de permiso

        camaraDbtn?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                abrirCamara("fDelantera")
            } else {
                pPermiso.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            } }

//codigo llamado camara trasera de p

        camaraTbtn?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                abrirCamara("fTrasera")
            } else {
                pPermiso.launch(Manifest.permission.CAMERA)
            }
        }
    }
//Codigo para obtencion de ubicacion

    private fun obtenerUbicacion() {
        obUbicacion.obtenerUbicacion(
            this,
            obtenerUbiTrue = { ubicacion ->Log.d("Ubicacion","Latitud: ${ubicacion.latitude}, Longitud: ${ubicacion.longitude}")
                guardarSolicitudDB(ubicacion)
            },
            obtenerUbiFalse = { excepcion ->Log.e("Ubicacion","Error al obtener la ubicacion: $excepcion")})
    }


//Codigo para obtecion de permisos
    private val pPermiso: ActivityResultLauncher<String> =
          registerForActivityResult(ActivityResultContracts.RequestPermission()) { permisoConcedido ->
              if (permisoConcedido) {Log.v("pPermiso", "autorizado")
                  obUbicacion
              } else {Log.v("peticionPermiso", "Permiso Denegado")}
           }


//codigo informacion a guardar base de datos

    private fun guardarSolicitudDB(ubicacion:Location){
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val solicitud = nuevacuenta(
                    nCompleto = nombreCET?.text.toString(),
                    rut = rutET?.text.toString() ,
                    fechaNacimiento = fechaNacimientoET?.text.toString() ,
                    email = emailET?.text.toString() ,
                    telefono = telefonoET?.text.toString(),
                    latitud = ubicacion.latitude,
                    longitud = ubicacion.longitude,
                    imagenCedulaFrente = "",
                    imagenCedulaTrasera = "",
                    fechaCreacionSolicitud = fechaActual
                )
                MainActivity.database.solicitudDao().insertSolicitud(solicitud)
            }
            if (!isFinishing && !isDestroyed){startActivity(Intent(this@CuentaActivity, MainActivity::class.java))
                finish()}
        }
    }


//Cdigo para toma de foto
    private val tFotoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val data: Intent? = resultado.data
                val imgUbicacion = data?.getStringExtra("ubicacionImagen")
                if (imgUbicacion != null){
                    if (tFoto == "fotoFrontal"){
                        imgCedulaF = imgUbicacion
                    } else if (tFoto == "fotoTrasera"){
                        imgCedulaT = imgUbicacion
                    }
                }
            } else {}
        }
//Codigo apertura de camara

    private fun abrirCamara(tipo: String){
        val intentCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        tFoto = tipo
        tFotoResult.launch(intentCamara)
    }
//codigo obtercion de ffecha
    private fun obtenerFechaActual(): String {
        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.getDefault())
        return formatoFecha.format(calendario.time)
    }
}









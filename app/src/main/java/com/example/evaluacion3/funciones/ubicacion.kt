package com.example.evaluacion3.funciones

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class ubicacion {

   private var ubiUsuario: FusedLocationProviderClient? = null

    private fun brakeUpdateUbicacion() {
        ubiUsuario?.let {
            it.removeLocationUpdates(object : LocationCallback() {})
        }
    }
   fun obtenerUbicacion(
        //codigo para posibles casos de obtener o no obtener la ubicacion
        contexto: Context,
        obtenerUbiTrue: (ubicacion: Location) -> Unit,
        obtenerUbiFalse: (exception: Exception) -> Unit,
        ) {
        try {ubiUsuario = LocationServices.getFusedLocationProviderClient(contexto)
            val solicitudUbicacion = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            val callbackUbicacion = object : LocationCallback() {
                override fun onLocationResult(resultadoUbicacion: LocationResult) {
                    super.onLocationResult(resultadoUbicacion)
                    if (resultadoUbicacion.locations.isNotEmpty())
                    {obtenerUbiTrue(resultadoUbicacion.locations.first())
                        brakeUpdateUbicacion()}}}
            ubiUsuario?.requestLocationUpdates(solicitudUbicacion, callbackUbicacion, null)
        }
        catch (excepcion: SecurityException) {
                obtenerUbiFalse(excepcion)}}
}
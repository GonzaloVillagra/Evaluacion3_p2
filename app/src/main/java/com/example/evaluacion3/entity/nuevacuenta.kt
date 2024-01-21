package com.example.evaluacion3.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_solicitudCuenta")
class nuevacuenta (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val nCompleto: String,
    val rut: String,
    val fechaNacimiento: String,
    val email: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val imagenCedulaFrente: String,
    val imagenCedulaTrasera: String,
    val fechaCreacionSolicitud: String
) {

}
package com.example.evaluacion3.funciones

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.evaluacion3.entity.nuevacuenta

//Codigo para la formacion de la tabla en la base de datos
@Dao
interface informacionDao {
    @Query("SELECT * FROM tabla_solicitudCuenta ORDER BY fechaCreacionSolicitud DESC")
    fun getTodasSolicitudes(): List<nuevacuenta>

    @Query("SELECT * FROM tabla_solicitudCuenta WHERE id = :id")
    fun getSolicitudPorId(id: Int): nuevacuenta?

    @Insert
    fun insertSolicitud(solicitud: nuevacuenta)

    @Delete
    fun deleteSolicitud(solicitud: nuevacuenta)

    @Update
    fun updateSolicitud(solicitud: nuevacuenta)
}

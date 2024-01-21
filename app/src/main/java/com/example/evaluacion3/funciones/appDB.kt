package com.example.evaluacion3.funciones

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evaluacion3.entity.nuevacuenta

//Codigo para la creacion de la base de datos

@Database(entities = [nuevacuenta::class], version = 1)
abstract class AppBaseDatos : RoomDatabase (){
    abstract fun solicitudDao(): informacionDao
}
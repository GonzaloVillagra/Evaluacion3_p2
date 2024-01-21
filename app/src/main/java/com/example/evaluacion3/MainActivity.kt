package com.example.evaluacion3


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.evaluacion3.funciones.AppBaseDatos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private val solicitudCuentabtn: Button?= null

    companion object {lateinit var database: AppBaseDatos}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val solicitudCuentabtn = findViewById<Button>(R.id.solicitudCuentabtn)
        solicitudCuentabtn.setOnClickListener {
            startActivity(Intent(this, CuentaActivity::class.java))
        }

        lifecycleScope.launch { withContext(Dispatchers.IO){
            database = Room.databaseBuilder(applicationContext,AppBaseDatos::class.java,"BancoBlanco").build()

        } }
        Log.d("CuentaActivity", "datos guardados")
    }
}

